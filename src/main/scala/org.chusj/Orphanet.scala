package org.chusj

import org.chusj.Hpo.jedisClient
import redis.clients.jedis.Jedis

import scala.xml.XML

object Orphanet extends App {

  val doc = XML.loadFile(args(1))
  val jedisClient = new Jedis()

  for {
    disorder  <- doc \\ "DisorderList" \\ "Disorder"

  } yield {
    val orphaNumber = disorder \ "OrphaNumber"
    val name = disorder \ "Name"

    println(s"DId=${disorder\@"id"}-ON=$orphaNumber-$name")


    for {
      genes <-  disorder \ "DisorderGeneAssociationList" \ "DisorderGeneAssociation" \ "Gene"
    } yield {
      //val symbol = genes \ "Symbol"
      val externalReferenceList = genes \ "ExternalReferenceList"
      val extRefMap = externalReferenceList \ "ExternalReference"
      val eId = extRefMap.find( node => ( node \ "Source").text == "Ensembl" ).map(_ \ "Reference")

      eId.map(_.text) match {
        case Some(s) => saveToRedis(s,s"Orph:${orphaNumber.text},${name.text}")
        case None => println("no Ensembl id")
      }
    }

    jedisClient.save()
    jedisClient.close()

  }


  def saveToRedis(ensId:String, panel: String): Unit = {
    println(s"\tid:$ensId-panel=$panel")
    jedisClient.sadd(s"id:$ensId",s"$panel" )

  }

}
