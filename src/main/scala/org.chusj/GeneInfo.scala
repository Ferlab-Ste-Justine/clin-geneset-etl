package org.chusj

import redis.clients.jedis.Jedis

import scala.io.Source

object GeneInfo extends App {


  val jedisClient = new Jedis()
  val filename = args(1)
  for (line <- Source.fromFile(filename).getLines) {
    if (!line.startsWith("#") ) {
      val words = line.split("\t")
      val ensID = words(5).trim.split("[|]", 0).last.replace("Ensembl:", "")

      for (ens <- words(5).trim.split("[|]", 0)) {
        val ensemblId = ens.replace("Ensembl:", "")
        if (!"-".equals(ensemblId) && ensemblId.startsWith("EN")) {
          println(ensemblId)

          val symbol = words(2)
          val alias = words(4).replaceAll("-", "").trim.split("[|]", 0).toList

          val map_location = words(7)
          val description = words(8)
          val typeOfGene = words(9)
          val geneId = words(1)
          val name = words(11)

          jedisClient.sadd(s"id:$ensemblId",s"symbol:$symbol" )
          jedisClient.sadd(s"id:$ensemblId",s"geneid:$geneId" )
          jedisClient.sadd(s"id:$ensemblId",s"name:$name" )
          jedisClient.sadd(s"id:$ensemblId",s"map_location:$map_location" )
          jedisClient.sadd(s"id:$ensemblId",s"type:$typeOfGene" )
          jedisClient.sadd(s"id:$ensemblId",s"desc:$description" )
          jedisClient.sadd(s"gene:$symbol", s"id:$ensemblId")

          alias.foreach( u => {
            if (!u.isEmpty) {
              //println(u)
              jedisClient.sadd(s"id:$ensemblId",s"alias:$u" )
              jedisClient.sadd(s"gene:$u", s"id:$ensemblId")
            }
          }  )
        }
      }
    }
  }

  jedisClient.save()
  jedisClient.close()

}
