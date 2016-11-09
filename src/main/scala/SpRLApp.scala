import java.io.File

import edu.illinois.cs.cogcomp.core.datastructures.textannotation.Constituent
import edu.illinois.cs.cogcomp.saul.classifier.Learnable
import edu.illinois.cs.cogcomp.saul.util.Logging

/** Created by Parisa on 7/29/16.
  */
object SpRLApp extends App with Logging {
  import SpRLClassifiers._
  import SpRLDataModel._

  val modelDir = "models/"
  val isTrain = true

  logger.info("population starts.")

  PopulateSpRLDataModel(isTrain)
  val trajectors = tokens().filter(x=> isTrajector(x).equals("true"))
  val landmarks = tokens().filter(x=> isLandmark(x).equals("true"))
  val spatialIndicators = tokens().filter(x=> isSpatialIndicator(x).equals("true"))

  logger.info("number of all sentences after population:" + sentences().size)
  logger.info("number of all tokens after population:" + tokens().size)
  logger.info("number of all trajectors after population:" + trajectors.size)
  logger.info("number of all landmarks after population:" + landmarks.size)
  logger.info("number of all spatialIndicators after population:" + spatialIndicators.size)

  runClassifier(trajectorClassifier, "trajectors")
  runClassifier(landmarkClassifier, "landmarks")
  runClassifier(spatialIndicatorClassifier, "spatialIndicators")

  def runClassifier(classifier: Learnable[Constituent], name: String): Unit = {
    classifier.modelDir = modelDir + name + File.separator
    if (isTrain) {
      logger.info("training " + name + "...")
      classifier.learn(100)
      classifier.save()
    }
    else {
      classifier.load()
      logger.info("testing " + name + " ...")
      classifier.test()
    }
    logger.info("done.")
  }

}