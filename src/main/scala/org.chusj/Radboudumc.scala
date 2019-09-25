package org.chusj

import redis.clients.jedis.Jedis


object Radboudumc extends App {


  val radMap: Map[String, String] = Map(
    "AKI" -> "AKI",
    "ALS" -> "ALS",
    "CILIOPATHIES" -> "CILIOPATHIES",
    "CONGENITAL HEART DISEASE" -> "CONGENITAL HEART DISEASE",
    "CRANIOFACIAL ANOMALIES" -> "CRANIOFACIAL ANOMALIES",
    "DISORDERS OF SEX DEVELOPMENT" -> "DISORDERS OF SEX DEVELOPMENT PANEL",
    "DYSKERATOSIS CONGENITA" -> "DKC",
    "EPILEPSY" -> "EPILEPSY PANEL",
    "HEARING IMPAIRMENT" -> "HEARING IMPAIRMENT",
    "HEART" -> "HEART GENE PANEL",
    "HEMOSTATIC/THROMBOTIC" -> "HEMOSTATICDISORDERS",
    "HEREDITARY CANCER" -> "HEREDITARY CANCER PANEL",
    "HYPOGONADOTROPIC HYPOGONADISM" -> "HYPOGONADOTROPIC HYPOGONADISM PANEL",
    "INTELLECTUAL DISABILITY" -> "INTELLECTUAL DISABILITY",
    "IRON METABOLISM" -> "IRON METABOLISM PANEL",
    "MENDELIOME GENE" -> "MENDELIOME GENE PANEL",
    "METABOLIC DISORDERS" -> "METABOLIC DISORDERS",
    "MITOCHONDRIAL DISORDERS" -> "MITOCHONDRIAL DISORDERS N",
    "MOVEMENT DISORDERS" -> "MOVEMENTDISORDERS",
    "MUSCLE DISORDERS" -> "MUSCLE DISORDERS",
    "NEUROPATHIES PANEL" -> "NEUROPATHIES PANEL",
    "PARKINSON" -> "PARKINSON",
    "PRECONCEPTION SCREENING" -> "PCS",
    "PRIMARY IMMUNODEFICIENCIES" -> "PRIMARY IMMUNODEFICIENCIES",
    "RENAL DISORDERS" -> "RENAL DISORDERS",
    "SHORT STATURE AND SKELETAL DYSPLASIA" -> "SHORT STATURE AND SKELETAL DYSPLASIA",
    "SKIN DISORDERS" -> "SKIN DISORDERS",
    "VISION DISORDERS" -> "VISION DISORDERS"

    )

  val newArgs: Array[String] = if (args.length == 0 ) {
    Array("rad", "RAD_Files", "_DG216.pdf")
  } else {
    args
  }

  val jedisClient = new Jedis()

  val folder = newArgs(1)
  val extension = newArgs(2)

  for {
    rad <- radMap
  } yield {

    val radboudumcGenePanelName = rad._1
    val filename  = folder +  "/" + rad._2 + extension
    println(s"---$radboudumcGenePanelName---")
    PdfToText.getListOfGeneFromPdf(filename).foreach(gene => {

      print(s"$gene-")
      val ensIds = jedisClient.smembers(s"gene:$gene")
      ensIds.forEach( ensId => {
        println(s"ensId:$ensId")

        jedisClient.sadd(s"$ensId",s"Rad:$radboudumcGenePanelName" )
      } )


    })

  }

  jedisClient.save()
  jedisClient.close()


}
