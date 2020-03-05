package org.chusj


object Main extends App {

  val etlToRun = args(0)
  val argumentsPresent = args.length >= 2 && !args(1).isEmpty

  etlToRun match {
    case "geneInfo" if argumentsPresent => GeneInfo.main(args)
    case "hpo" if argumentsPresent => Hpo.main(args)
    case "orpha" if argumentsPresent => Orphanet.main(args)
    case "rad" if argumentsPresent => Radboudumc.main(args)
    case "etl" if argumentsPresent => ETL.main(args)
    case _ =>
      println("missing or wrong etl paramater(s)")
      println(
        """Usages:
          |  geneInfo Homo_sapiens.gene_info.txt OR
          |  hpo ALL_SOURCES_ALL_FREQUENCIES_genes_to_phenotype.txt OR
          |  orpha en_product6.xml
          |  rad foldersWereRadPDFFilesIs extension (i.e.) RAD_Files _DG216.pdf
        """.stripMargin)

  }

}
