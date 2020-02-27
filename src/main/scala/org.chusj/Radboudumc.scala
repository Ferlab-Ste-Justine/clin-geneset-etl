package org.chusj

import redis.clients.jedis.Jedis


object Radboudumc extends App {


  val radMap: Map[String, String] = Map(

    "ALS" -> "ALS",
    "Ciliopathies" -> "CILIOPATHIES",
    "Congenital Hearth Disease" -> "CONGENITAL-HEART-DISEASE",
    "Cranofacial Anomalies" -> "CRANIOFACIAL-ANOMALIES-PANEL",
    "Disorders Of Sex Development" -> "DISORDERS-OF-SEX-DEVELOPMENT-PANEL",
    "Dyskeratosis Congenita" -> "DKC",
    "Epilepsy" -> "EPILEPSY-PANEL",
    "Fetal Akinesia" -> "AKI",
    "Hearing Impairment" -> "HEARING-IMPAIRMENT",
    "Hearth" -> "HEART-GENE-PANEL",
    "Hemostatic/Thrombotic" -> "HEMOSTATICDISORDERS",
    "Hereditary Cancer" -> "HEREDITARY-CANCER-PANEL",
    "Hereditary Bone Marrow Failure" -> "HEREDITARYBONEMARROWFAILURE",
    "Hypogonadotropic Hypogonadism" -> "HYPOGONADOTROPIC-HYPOGONADISM-PANEL",
    "Intelectual Disability" -> "INTELLECTUAL-DISABILITY",
    "Iron Metabolism" -> "IRON-METABOLISM-PANEL",
    "Liver Disorders" -> "LIVER",
    "Mendeloime Gene" -> "MENDELIOME-GENE-PANEL",
    "Mitochondrial Disorders" -> "MITOCHONDRIAL-DISORDERS-N",
    "Movement Disorders" -> "MOVEMENTDISORDERS",
    "Muscle Disorders" -> "MUSCLE-DISORDERS",
    "Neuropathies Panel" -> "NEUROPATHIES-PANEL",
    "Parkinson" -> "PARKINSON",
    "Preconception Screening" -> "PCS",
    "Primary Immunodeficiencies" -> "PRIMARY-IMMUNODEFICIENCIES",
    "Renal Disorders" -> "RENAL-DISORDERS",
    "Sonic Hedgehog Medulloblastoma" -> "SHHM",
    "Short Stature And Skeletal Dysplasia" -> "SHORT-STATURE-AND-SKELETAL-DYSPLASIA",
    "Skin Disorders" -> "SKIN-DISORDERS",
    "Vision Disorders" -> "VISIONDISORDERS"

    )

  val newArgs: Array[String] = if (args.length == 0 ) {
    Array("rad", "RAD_Files", "_DG217.pdf")
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
