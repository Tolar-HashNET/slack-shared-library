#!/usr/bin/env groovy

/**
 * Send notifications based on build status string
 */

def call(String buildStatus = 'STARTED') {
  // build status of null means successful
  buildStatus =  buildStatus ?: 'SUCCESSFUL'

  // Default values
  def color = 'RED'
  def colorCode = '#FF0000'
  def subject = "${buildStatus}: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]'"
  def summary = "${subject} (${env.BUILD_URL})"
  def details = """<p>${buildStatus}: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]':</p>
    <p>Check console output at &QUOT;<a href='${env.BUILD_URL}'>${env.JOB_NAME} [${env.BUILD_NUMBER}]</a>&QUOT;</p>"""

  // Override default values based on build status
  if (buildStatus == 'STARTED') {
    color = 'YELLOW'
    colorCode = '#FFFF00'
    buildStatus = 'Started'
  } else if (buildStatus == 'SUCCESSFUL') {
    color = 'GREEN'
    colorCode = '#00FF00'
    buildStatus = 'Successful'
  } else if (buildStatus == 'FAILURE') {
    buildStatus = 'FAILED'
  }

  // Send notifications
  try
  {
    slackSend (color: colorCode, message: summary)
  }
  catch (error)
  {
    echo "Slack notification failed: $error"
    currentBuild.result = 'UNSTABLE'
  }
}
