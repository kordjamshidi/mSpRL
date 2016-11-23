import edu.illinois.cs.cogcomp.core.datastructures.ViewNames
import edu.illinois.cs.cogcomp.core.datastructures.textannotation.Constituent
import edu.illinois.cs.cogcomp.edison.features.factory.{ParseHeadWordPOS, SubcategorizationFrame}
import edu.illinois.cs.cogcomp.edison.features.{FeatureExtractor, FeatureUtilities}

import scala.collection.JavaConversions._

/** Created by taher on 7/28/16.
  */
object SpRLSensors {
  val parseView = ViewNames.PARSE_STANFORD
  //TODO: should be moved to CommonSensores
//  def getPOS(x: Constituent): String = {
//    WordFeatureExtractorFactory.pos.getFeatures(x).mkString
//  }
//  def getLemma(x: Constituent): String = {
//    WordFeatureExtractorFactory.lemma.getFeatures(x).mkString
//  }
  def fexFeatureExtractor(x: Constituent, fex: FeatureExtractor): String = {
    FeatureUtilities.getFeatureSet(fex, x).mkString(",")
  }
 def HeadwordPOS(x: Constituent): String = {
  fexFeatureExtractor(x, new ParseHeadWordPOS(parseView))
  }
def Subcategorization(x: Constituent) = {
  
  fexFeatureExtractor(x, new SubcategorizationFrame(parseView))
}
}
