package org.chusj

import redis.clients.jedis.Jedis

import scala.io.Source

object hpo extends App {

  val jedis = new Jedis()
  val filename = args(1)

  for (line <- Source.fromFile(filename).getLines) {
    if (!line.startsWith("#") ) {
      val words = line.split("\t")
      val geneId = words(1)
        // get ensemblid from redis
      val ensIds = jedis.smembers(s"gene:$geneId")
      ensIds.forEach( ensId => {
        println(s"ensId:$ensId")

        val hpoTermName = words(2)
        val hpoId = words(3)
        jedis.sadd(s"$ensId",s"$hpoId,$hpoTermName" )
      } )
    }

  }

  jedis.save()
  jedis.close()

}
