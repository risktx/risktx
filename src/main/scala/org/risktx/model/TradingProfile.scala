package org.risktx.model

import net.liftweb.mapper._

class TradingProfile extends LongKeyedMapper[TradingProfile] with IdPK {
  def getSingleton = TradingProfile

  object owner extends MappedLongForeignKey(this, User) {
    override def dbIndexed_? = true
  }

  object name extends MappedString(this, 100)

  object description extends MappedString(this, 300)
}

object TradingProfile extends TradingProfile with LongKeyedMetaMapper[TradingProfile] {
  def findByName (owner : User, name : String) : List[TradingProfile] = 
    TradingProfile.findAll(By(TradingProfile.owner, owner.id.is), By(TradingProfile.name, name))
}
