import edu.illinois.cs.cogcomp.core.datastructures.textannotation.Constituent
import edu.illinois.cs.cogcomp.edison.features.factory.WordFeatureExtractorFactory
import edu.illinois.cs.cogcomp.edison.features.{FeatureExtractor, FeatureUtilities}

import scala.collection.JavaConversions._

/** Created by taher on 7/28/16.
  */
object SpRLSensors {
  //TODO: should be moved to CommonSensores
  def getPOS(x: Constituent): String = {
    WordFeatureExtractorFactory.pos.getFeatures(x).mkString
  }
  def getLemma(x: Constituent): String = {
    WordFeatureExtractorFactory.lemma.getFeatures(x).mkString
  }
  def fexFeatureExtractor(x: Constituent, fex: FeatureExtractor): String = {
    FeatureUtilities.getFeatureSet(fex, x).mkString(",")
  }
}