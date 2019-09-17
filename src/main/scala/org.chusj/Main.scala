package org.chusj


object Main extends App {

  val etlToRun = args(0)
  val filePresent = args.length >= 2 && !args(1).isEmpty

  etlToRun match {
    case "geneInfo" if filePresent => GeneInfo.main(args)
    case "hpo" if filePresent => Hpo.main(args)
    case "orpha" if filePresent => Orphanet.main(args)
    case _ =>
      println("missing or wrong etl paramater(s)")
      println(
        """Usages:
          |  geneInfo Homo_sapiens.gene_info.txt OR
          |  hpo ALL_SOURCES_ALL_FREQUENCIES_genes_to_phenotype.txt OR
          |  orpha en_product6.xml
        """.stripMargin)

  }

}