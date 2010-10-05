package org.risktx.domain.model.administration

import net.liftweb.http.{S, SessionVar}
import net.liftweb.mapper._
import net.liftweb.common._
import net.liftweb.util._
import net.liftweb.util.Mailer._
import net.liftweb.sitemap._
import net.liftweb.sitemap.Loc._

/**
 * User entity based on Lift's MegaProtoUser
 **/
class User extends MegaProtoUser[User] {
  def getSingleton = User
}

/**
 * User companion object with Helper methods
 **/
object User extends User with MetaMegaProtoUser[User] with Logger {
  // DB table name
  override def dbTableName = "users"

  override def screenWrap = Full(<lift:surround with="public" at="content">
      <lift:bind/>
  </lift:surround>)

  override def signupFields = List(firstName, lastName, email, password)

  // define the order fields will appear in forms and output
  override def fieldOrder = List(id, firstName, lastName, email, password)

  // configure email validations for registration - default is enabled
  override def skipEmailValidation = !(Props.getBool("user.registration.email.validation", false))

  override def homePage = whereAfterLogin.is openOr super.homePage

  override def sendPasswordReset(email: String) {
    getSingleton.find(By(this.email, email)) match {
      case Full(user) if user.validated =>
        user.uniqueId.reset().save
        val resetLink = S.hostAndPath +
                passwordResetPath.mkString("/", "/", "/") + user.uniqueId

        val email: String = user.email

        val msgXml = passwordResetMailBody(user, resetLink)
        Mailer.sendMail(From(emailFrom), Subject(passwordResetEmailSubject),
          To(user.email), xmlToMailBodyType(msgXml))
        info("Email sent..." + emailFrom)
        info("Email sent..." + passwordResetEmailSubject)
        info("Email sent..." + user.email)
        info("Email sent..." + msgXml)
        info("Email sent..." + bccEmail.toList)
        S.notice(S.??("password.reset.email.sent"))

      case Full(user) =>
        sendValidationEmail(user)
        S.notice(S.??("account.validation.resent"))
        S.redirectTo(homePage)

      case _ => S.error(S.??("email.address.not.found"))
    }
  }

  object whereAfterLogin extends SessionVar[Box[String]](Empty)

  /**
   * The menu item for logout (make this "Empty" to disable)
   */
  override def logoutMenuLoc: Box[Menu] =
  Full(Menu(Loc("Logout", logoutPath, S.??("logout"),
                Template(() => wrapIt(logout)),
                testLogginIn)))

  /**
   * The menu item for editing the user (make this "Empty" to disable)
   */
  override def editUserMenuLoc: Box[Menu] =
  Full(Menu(Loc("EditUser", editPath, S.??("edit.user"),
                Template(() => wrapIt(editFunc.map(_()) openOr edit)),
                testLogginIn)))

  /**
   * The menu item for changing password (make this "Empty" to disable)
   */
  override def changePasswordMenuLoc: Box[Menu] =
  Full(Menu(Loc("ChangePassword", changePasswordPath,
                S.??("change.password"),
                Template(() => wrapIt(changePassword)),
                testLogginIn)))

  override lazy val sitemap: List[Menu] =
  List(loginMenuLoc, createUserMenuLoc,
       lostPasswordMenuLoc, validateUserMenuLoc,
       resetPasswordMenuLoc, editUserMenuLoc,
       changePasswordMenuLoc, logoutMenuLoc).flatten(a => a)  
}

