package org.chusj

import redis.clients.jedis.Jedis

import scala.io.Source

object Omim extends App {

  val jedisClient = new Jedis()
  val filename = args(1)
  var transmissionSet : Set[String] = Set()
  var hpoTerms = 0


  for (line <- Source.fromFile(filename).getLines) {
    if (!line.startsWith("#") ) {
      val words = line.split("\t")

      if (words.length > 12) {
        val geneMim = words(5)
        val phenotypes = words(12)

        if (phenotypes.length > 0) {
          val phenos = phenotypes.split(";")

          phenos.foreach(pheno => {

            val pattern = "\\d{6}".r
            val phenoMim = (pattern findFirstIn pheno).mkString
            if (phenoMim.length >0) {
              val position = pheno.indexOf(phenoMim)

              val phenoName = if (position >0 ) pheno.substring(0, position -2).trim else ""
              val rest = if (position > 0 ) pheno.substring(position+9).trim else ""

              transmissionSet = Set()
              if (rest.length > 1) {
                if (!rest.contains("?")) {
                  if (rest.contains("Y-linked")) transmissionSet += "YL"
                  if (rest.contains("X-linked")) transmissionSet += "XL"
                  if (rest.contains("Y-linked recessive")) transmissionSet += "YLR"
                  if (rest.contains("Y-linked dominant")) transmissionSet += "YLD"
                  if (rest.contains("X-linked dominant")) transmissionSet += "XLD"
                  if (rest.contains("X-linked recessive")) transmissionSet += "XLR"
                  if (rest.contains("Pseudoautosomal recessive")) transmissionSet += "PR"
                  if (rest.contains("Pseudoautosomal dominant")) transmissionSet += "PD"
                  if (rest.contains("Autosomal recessive")) transmissionSet += "AR"
                  if (rest.contains("Autosomal dominant")) transmissionSet += "AD"
                  if (rest.contains("Mitochondrial")) transmissionSet += "Mi"
                  if (rest.contains("Multifactorial")) transmissionSet += "Mu"
                  if (rest.contains("Inherited chromosomal imbalance")) transmissionSet += "ICB"
                  if (rest.contains("Somatic mutation")) transmissionSet += "Smu"
                  if (rest.contains("Isolated cases")) transmissionSet += "IC"
                  if (rest.contains("Somatic mosaicism")) transmissionSet += "SMo"
                  if (rest.contains("Digenic recessive")) transmissionSet += "DR"
                }
                else {
                  if (rest.contains("X-linked")) transmissionSet += "?XL"
                  if (rest.contains("Autosomal dominant")) transmissionSet += "?AD"
                }
              }

              if (rest.length > 1 && transmissionSet.isEmpty) {
                println(s"id:$geneMim>pheno='${pheno.trim}'")
                println( s"$phenoMim->$phenoName+$transmissionSet" )
              }


              val ensIds = jedisClient.smembers(s"omim:$geneMim")
              ensIds.forEach( ensId => {
                println(s"ensId:$ensId")
                val transmission = transmissionSet.mkString(",")
                println( s"\tid:$geneMim>op:$phenoMim$phenoName;$transmission" )
                 jedisClient.sadd(s"$ensId", s"op:$phenoMim$phenoName;$transmission")
              })
            }
          })
        }
      }
    }
  }
  jedisClient.save()
  jedisClient.close()

}
