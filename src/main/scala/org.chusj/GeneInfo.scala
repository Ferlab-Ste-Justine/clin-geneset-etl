package org.chusj

import redis.clients.jedis.Jedis

import scala.io.Source

object GeneInfo extends App {

  val jedisClient = new Jedis()
  val filename = args(1)
  val geneInfoSource = Source.fromFile(filename)

  for (line <- geneInfoSource.getLines) {
    if (!line.startsWith("#") ) {
      val words = line.split("\t")

      var omim : Set[String] = Set()
      var hgnc : Set[String] = Set()
      var ensIds : Set[String] = Set()

      for (word <- words(5).trim.split("[|]", 0)) {
        if (word.startsWith("MIM:")) {
          omim = omim ++ Set(word.replace("MIM:", ""))
        }
        if (word.startsWith("HGNC:HGNC:")) {
          hgnc = hgnc ++ Set( word.replace("HGNC:HGNC:", "") )
        }
        if (word.startsWith("Ensembl:")) {
          val ensId = word.replace("Ensembl:", "")
          if (!"-".equals(ensId) && ensId.startsWith("EN")) {
            ensIds = ensIds ++ Set(ensId)
          }
        }
      }

      for (ensemblId <- ensIds) {
        println(ensemblId)
        val symbol = words(2)
        val alias = words(4).replaceAll("-", "").trim.split("[|]", 0).toList

        val map_location = words(7)
        val description = words(8)
        val typeOfGene = words(9)
        val geneId = words(1)
        val name = words(11)

        for (omimId <- omim) {
          jedisClient.sadd(s"id:$ensemblId",s"omim:$omimId" )
        }
        for (hgncId <- hgnc) {
          jedisClient.sadd(s"id:$ensemblId",s"hgnc:$hgncId" )
        }
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

  geneInfoSource.close()
  jedisClient.save()
  jedisClient.close()

}
