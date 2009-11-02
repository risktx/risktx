package org.risktx.model

import net.liftweb.mapper._

/**
* A TradingProfile is an entity which contains all the information about a
* Trading party, such as what messages they are registered to send and receive,
* their certificate, endpoint and other information
**/
class TradingProfile extends LongKeyedMapper[TradingProfile] with IdPK {
  def getSingleton = TradingProfile

  /**
  * The owner of the TradingProfile, foreign key to entity
  **/
  object owner extends MappedLongForeignKey(this, User) {
    override def dbIndexed_? = true
  }

  /**
  * Trading Party Name
  **/
  object name extends MappedString(this, 100)

  /**
  * Description of Trading Party
  **/
  object description extends MappedString(this, 300)
}

/**
* TradingProfile companion object with Helper methods
**/
object TradingProfile extends TradingProfile with LongKeyedMetaMapper[TradingProfile] {

  /**
  * Helper method to find a TradingProfile
  *
  * @param Owner  The owner of the profile
  * @param name   The name of the TradingProfile
  **/
  def findByName (owner : User, name : String) : List[TradingProfile] = 
    TradingProfile.findAll(By(TradingProfile.owner, owner.id.is), By(TradingProfile.name, name))
}
