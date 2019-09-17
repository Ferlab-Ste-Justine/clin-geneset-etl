package org.chusj

import redis.clients.jedis.Jedis

import scala.xml.XML

object Orphanet extends App {

  val doc = XML.loadFile(args(1))
  val jedisClient = new Jedis()

  for {
    disorder  <- doc \\ "DisorderList" \\ "Disorder"
    orphaNumber <- disorder \ "OrphaNumber"
    name <- disorder \ "Name"
    genes <-  disorder \ "DisorderGeneAssociationList" \ "DisorderGeneAssociation" \ "Gene"
    externalReferenceList = genes \ "ExternalReferenceList"
    extRefMap = externalReferenceList \ "ExternalReference"
    eId = extRefMap.find( node => ( node \ "Source").text == "Ensembl" ).map(_ \ "Reference")

  } yield {

    println(s"DId=${disorder\@"id"}-ON=$orphaNumber-$name")

    eId.map(_.text) match {
      case Some(s) => saveToRedis(s,s"Orph:${orphaNumber.text},${name.text}")
      case None => println("no Ensembl id")
    }

    jedisClient.save()
    jedisClient.close()

  }

  def saveToRedis(ensId:String, panel: String): Unit = {
    println(s"\tid:$ensId-panel=$panel")
    jedisClient.sadd(s"id:$ensId",s"$panel" )

  }

}
