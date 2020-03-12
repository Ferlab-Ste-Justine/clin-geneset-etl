package org.chusj

import java.util

import org.apache.spark.sql.SparkSession
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.sql.functions._
import org.chusj.GeneInfo.args
import redis.clients.jedis.Jedis

import scala.io.Source


object ETL extends App {

  println("running ETL")

  case class gene(ensemblId: String, biotype: String, geneId: Int, alias: Seq[String], location: String,
                  geneSymbol: String)

  val jedisClient = new Jedis()
  //val filename = args(1)
  val filename = "Homo_sapiens.gene_info.txt"
  for (line <- Source.fromFile(filename).getLines) {
    if (!line.startsWith("#") ) {
      val words = line.split("\t")
      val ensemblId = words(5).trim.split("[|]", 0).last.replace("Ensembl:", "")

      if (!"-".equals(ensemblId) && ensemblId.startsWith("EN")) {
        println(ensemblId)


        val sMembers: util.Set[String] = jedisClient.smembers("id:" + ensemblId)

        sMembers.forEach( println(_))

      }
    }
  }

  jedisClient.save()
  jedisClient.close()



}
