#!/usr/bin/env groovy

/**
<<<<<<< HEAD
 * Send notifications based on build status string
 */
def call(String buildStatus = 'STARTED') {
  // build status of null means successful
  buildStatus = buildStatus ?: 'SUCCESS'
=======
* notify slack and set message based on build status
*/
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import hudson.tasks.test.AbstractTestResultAction;
import hudson.model.Actionable;

def call(String buildStatus = 'STARTED') {

  // buildStatus of null means successfull
  buildStatus = buildStatus ?: 'SUCCESSFUL'
  //env.CHANGE_BRANCH ?: env.GIT_BRANCH ?: scm.branches[0]?.name?.split('/')[1] ?: 'UNKNOWN'
>>>>>>> slack-custom-messages

  // Default values
  def colorName = 'RED'
  def colorCode = '#FF0000'
<<<<<<< HEAD
  def subject = "${buildStatus}: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]'"
  def summary = "${subject} (${env.BUILD_URL})"
  def details = """<p>${buildStatus}: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]':</p>
    <p>Check console output at &QUOT;<a href='${env.BUILD_URL}'>${env.JOB_NAME} [${env.BUILD_NUMBER}]</a>&QUOT;</p>"""
=======
  def subject = "${buildStatus}: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}] (<${env.RUN_DISPLAY_URL}|Open>) (<${env.RUN_CHANGES_DISPLAY_URL}|  Changes>)'"
  def title = "${env.JOB_NAME} Build: ${env.BUILD_NUMBER}"
  def title_link = "${env.RUN_DISPLAY_URL}"
  //def branchName = env.CHANGE_BRANCH ?: env.GIT_BRANCH ?: scm.branches[0]?.name?.split('/')[1] ?: 'UNKNOWN'
  //def branchName = sh(returnStdout: true, script: 'git rev-parse --abbrev-ref HEAD').trim()

  def commit = sh(returnStdout: true, script: 'git rev-parse HEAD')
  def author = sh(returnStdout: true, script: "git --no-pager show -s --format='%an'").trim()
  def branchName = "${env.GIT_BRANCH}"
  def message = sh(returnStdout: true, script: 'git log -1 --pretty=%B').trim()
>>>>>>> slack-custom-messages

  // Override default values based on build status
  if (buildStatus == 'STARTED') {
    color = 'YELLOW'
    colorCode = '#FFFF00'
  } else if (buildStatus == 'SUCCESS') {
    color = 'GREEN'
    colorCode = '#00FF00'
  } else {
    color = 'RED'
    colorCode = '#FF0000'
  }
<<<<<<< HEAD

  // Send notifications
  slackSend (color: colorCode, message: summary)
=======
  def testSummaryRaw = getTestSummary()
  // format test summary as a code block
  def testSummary = "```${testSummaryRaw}```"
  println testSummary.toString()

  JSONObject attachment = new JSONObject();
  attachment.put('author',"jenkins");
  attachment.put('author_link',"https://danielschaaff.com");
  attachment.put('title', title.toString());
  attachment.put('title_link',title_link.toString());
  attachment.put('text', subject.toString());
  attachment.put('fallback', "fallback message");
  attachment.put('color',colorCode);
  attachment.put('mrkdwn_in', ["fields"])
  // JSONObject for branch
  JSONObject branch = new JSONObject();
  branch.put('title', 'Branch');
  branch.put('value', branchName.toString());
  branch.put('short', true);
  // JSONObject for author
  JSONObject commitAuthor = new JSONObject();
  commitAuthor.put('title', 'Author');
  commitAuthor.put('value', author.toString());
  commitAuthor.put('short', true);
  // JSONObject for branch
  JSONObject commitMessage = new JSONObject();
  commitMessage.put('title', 'Commit Message');
  commitMessage.put('value', message.toString());
  commitMessage.put('short', false);
  // JSONObject for test results
  JSONObject testResults = new JSONObject();
  testResults.put('title', 'Test Summary')
  testResults.put('value', testSummary.toString())
  testResults.put('short', false)
  attachment.put('fields', [branch, commitAuthor, commitMessage, testResults]);
  JSONArray attachments = new JSONArray();
  attachments.add(attachment);
  println attachments.toString()

  // Send notifications
  slackSend (color: colorCode, message: subject, attachments: attachments.toString())
>>>>>>> slack-custom-messages
}
