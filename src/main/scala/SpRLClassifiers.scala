import edu.illinois.cs.cogcomp.core.datastructures.textannotation.Constituent
import edu.illinois.cs.cogcomp.lbjava.learn.SparseNetworkLearner
import edu.illinois.cs.cogcomp.saul.classifier.Learnable
import edu.illinois.cs.cogcomp.saul.datamodel.property.Property

/** Created by taher on 7/30/16.
  */
object SpRLClassifiers {

  import SpRLDataModel._
  object spatialIndicatorClassifier extends Learnable[Constituent](tokens) {

    def label: Property[Constituent] = isSpatialIndicator
    override def feature = using(lemma, posTag, headword, subcategorization)
    override lazy val classifier = new SparseNetworkLearner()
  }
  object trajectorClassifier extends Learnable[Constituent](tokens) {

    def label: Property[Constituent] = isTrajector
    override def feature = using(lemma, posTag, headword, subcategorization)
    override lazy val classifier = new SparseNetworkLearner()
  }
  object landmarkClassifier extends Learnable[Constituent](tokens) {

    def label: Property[Constituent] = isLandmark
    override def feature = using(lemma, posTag, headword, subcategorization)
    override lazy val classifier = new SparseNetworkLearner()
  }
}