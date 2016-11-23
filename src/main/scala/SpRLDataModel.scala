
import SpRLSensors._
import edu.illinois.cs.cogcomp.core.datastructures.textannotation.{Constituent, Sentence}
import edu.illinois.cs.cogcomp.saul.datamodel.DataModel
import edu.illinois.cs.cogcomp.saulexamples.nlp.CommonSensors._

/** Created by taher on 7/28/16.
  */
object SpRLDataModel extends DataModel {

  val sentences = node[Sentence]
  val tokens = node[Constituent]

  val sentencesToTokens = edge(sentences, tokens)

  sentencesToTokens.addSensor(sentenceToTokens _)

  // Classification labels
  val isSpatialIndicator = property(tokens) {
    x: Constituent => x.getTextAnnotation.getView("sprl-SpatialIndicator").getLabelsCovering(x).contains("SpatialIndicator")
  }
  val isLandmark = property(tokens) {
    x: Constituent => x.getTextAnnotation.getView("sprl-Landmark").getLabelsCovering(x).contains("Landmark")
  }
  val isTrajector = property(tokens, "tr") {
    x: Constituent => x.getTextAnnotation.getView("sprl-Trajector").getLabelsCovering(x).contains("Trajector")
  }

  // features
  val posTag = property(tokens) {
    x: Constituent => getPosTag(x)
  }

  val lemma = property(tokens) {
    x: Constituent => getLemma(x)
  }

  val subcategorization = property(tokens) {
    x: Constituent =>  Subcategorization(x)
  }

  val headword = property(tokens) {
    x: Constituent => HeadwordPOS(x)
  }

}