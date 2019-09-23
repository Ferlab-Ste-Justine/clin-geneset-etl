package org.chusj

import redis.clients.jedis.Jedis


object Radboudumc extends App {

  val jedisClient = new Jedis()
  val filename = "ALS.pdf" //args(1)
  var previousGene = ""


  PdfToText.getListOfGeneFromPdf(filename).foreach(gene => {

    print(s"$gene-")

  })


}
