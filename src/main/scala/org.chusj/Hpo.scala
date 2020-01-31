package org.chusj

import redis.clients.jedis.Jedis

import scala.io.Source

object Hpo extends App {

  val jedisClient = new Jedis()
  val filename = args(1)
  var geneSet : Set[String] = Set()
  var hpoTerms = 0


  for (line <- Source.fromFile(filename).getLines) {
    if (!line.startsWith("#") ) {
      val words = line.split("\t")
      val geneId = words(1)
        // get ensemblid from redis
      val ensIds = jedisClient.smembers(s"gene:$geneId")
      geneSet += geneId
      hpoTerms += 1
      ensIds.forEach( ensId => {
        println(s"ensId:$ensId")

        val hpoTermName = words(2)
        val hpoId = words(3)
        jedisClient.sadd(s"$ensId",s"$hpoId,$hpoTermName" )

      } )
    }

  }
  val avg: Double = hpoTerms / geneSet.size
  println(s"quantity of gene is ${geneSet.size} and numbers of terms is $hpoTerms so avg is $avg" )

  jedisClient.save()
  jedisClient.close()

}
