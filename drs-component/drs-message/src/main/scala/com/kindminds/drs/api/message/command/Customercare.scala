package com.kindminds.drs.api.message.command

import com.kindminds.drs.api.message.Command


case class UpdateCustomercareCase(caseId: Int) extends Command

case class SaveCustomercareCase(caseId: Int) extends Command

case class DeleteCustomercareCase(caseId: Int) extends Command

case class RefreshESCustomercareCase(caseId:Int) extends  Command

case class DeleteESCustomercareCase(caseId:Int) extends  Command


case class UpdateCustomercareCaseIssue(issueId: Int) extends Command

case class SaveCustomercareCaseIssue(issueId: Int) extends Command

case class DeleteCustomercareCaseIssue(issueId: Int) extends Command

case class RefreshESCustomercareCaseIssue(issueId:Int) extends  Command

case class DeleteESCustomercareCaseIssue(issueId:Int) extends  Command