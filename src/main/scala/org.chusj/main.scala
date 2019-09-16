package org.chusj


object main extends App {

  println("let's start")

  val etlToRun = args(0)

  etlToRun match {
    case "geneInfo" => geneInfo.main(args)
    case "hpo" => hpo.main(args)
    case _ => println("missing or wrong etl paramater")
  }

}