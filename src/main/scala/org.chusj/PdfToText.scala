package org.chusj

import java.io._

import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.text.PDFTextStripper

import scala.collection.mutable.ListBuffer
import scala.util.Try


object PdfToText {

  //System.setProperty("java.awt.headless", "true")

  def main(args: Array[String]) {
    if (args.length != 3) printUsageAndExit()

    val startPage = args(0).toInt
    val endPage = args(1).toInt
    val filename = args(2)

    // sanity check
    if (startPage > endPage) printUsageAndExit()

    val text:Option[String] = getTextFromPdf(startPage, endPage, filename)
    val strLst = getGeneList(text)
    println(strLst)



//    text match {
//      case Some(s) => {
//        println(s"length=${s.length}")
//        //println(s)
//
//        val line = s.split("\n")
//        line.foreach(l => {
//          //println(l)
//          val word = l.split(" ")
////          word.foreach( word => {
////            print(s"'${word.trim}' ")
////          })
//
//
//          val test1: Try[Int] = Try {
//            word(1).split(",")(0).toInt
//          }
//          val test2: Try[Int] = Try {
//            word(2).split("\\.")(0).toInt
//          }
//          if (test1.isSuccess && test2.isSuccess) {
//            println(s"Found gene:${word(0)}")
//            geneCount += 1;
//          }
//
//          println("---")
//        }
//        )
//
//
//      }
//      case None => println("")
//    }
//    println(s"geneCount=$geneCount")



  }

  def getListOfGeneFromPdf(fileName : String) : ListBuffer[String] = {

    getGeneList(getTextFromPdf(0, 20, fileName))

  }

  def printUsageAndExit() {
    println("")
    println("Usage: pdftotext startPage endPage filename")
    println("       (endPage must be >= startPage)")
    System.exit(1)
  }

  def getTextFromPdf(startPage: Int, endPage: Int, filename: String): Option[String] = {
    try {
      val pdf = PDDocument.load(new File(filename))
      val stripper = new PDFTextStripper
      stripper.setStartPage(startPage)
      stripper.setEndPage(endPage)
      Some(stripper.getText(pdf))
    } catch {
      case t: Throwable =>
        t.printStackTrace()
        None
    }
  }


  def getGeneList(text:Option[String]) : ListBuffer[String] = {
    var strLst = new ListBuffer[String]

    text match {
      case Some(s) =>
        val line = s.split("\n")
        line.foreach(l => {
          //println(l)
          val word = l.split(" ")
          //          word.foreach( word => {
          //            print(s"'${word.trim}' ")
          //          })


          val test1: Try[Int] = Try {
            word(1).split(",")(0).toInt
          }
          val test2: Try[Int] = Try {
            word(2).split("\\.")(0).toInt
          }
          if (test1.isSuccess && test2.isSuccess) {
            //println(s"Found gene:${word(0)}")
            strLst += word(0)
          }

          //println("---")
        }
        )
        //println(strLst)
        strLst


      case None => strLst


    }



  }






}