import java.util.Properties

import edu.illinois.cs.cogcomp.core.datastructures.textannotation._
import edu.illinois.cs.cogcomp.nlp.common.PipelineConfigurator._
import edu.illinois.cs.cogcomp.saulexamples.nlp.SpatialRoleLabeling.SpRL2013.SpRL2013Document
import edu.illinois.cs.cogcomp.saulexamples.nlp.SpatialRoleLabeling.{SpRLAnnotation, SpRLDataReader}
import edu.illinois.cs.cogcomp.saulexamples.nlp.TextAnnotationFactory

/** Created by taher on 7/28/16.
  */
object PopulateSpRLDataModel {
  def apply(isTrain: Boolean) = {

    SpRLDataModel.sentences.populate(readSpRLDocuments(), train = isTrain)

    def readSpRLDocuments(): List[Sentence] = {
      val path = if (isTrain)
        "data/IAPR TC-12/gold"
      else  "data/IAPR TC-12/train"


      //YOUR EXAMPLE
      val reader = new SpRLDataReader(path, classOf[SpRL2013Document])
      reader.readData()
      val settings = new Properties()
      TextAnnotationFactory.disableSettings(settings, USE_SRL_NOM, USE_NER_ONTONOTES, USE_SRL_VERB, USE_SHALLOW_PARSE,
        USE_STANFORD_DEP, USE_NER_CONLL)
      val as = TextAnnotationFactory.createPipelineAnnotatorService(settings)

      val sentences = ListBuffer[Sentence]()
      reader.documents.asScala.foreach(doc => {
        var offset = 0
        doc.getTEXT.getContent.split("(?<=.\\n\\n)").foreach(sentence => {
          val ta = TextAnnotationFactory.createTextAnnotation(as, "", "", sentence,
            "sprl-Trajector", "sprl-Landmark", "sprl-SpatialIndicator")
          val offsetEnd = offset + sentence.length

          val trajectors = getSentenceAnnotaions(doc.getTAGS.getTRAJECTOR.asScala, offset, offsetEnd)
          SetSpRLLabels(ta, trajectors, "Trajector", offset, offset + sentence.length)

          val sp = getSentenceAnnotaions(doc.getTAGS.getSPATIALINDICATOR.asScala, offset, offsetEnd)
          SetSpRLLabels(ta, sp, "SpatialIndicator", offset, offset + sentence.length)

          val landmarks = getSentenceAnnotaions(doc.getTAGS.getLANDMARK.asScala, offset, offsetEnd)
          SetSpRLLabels(ta, landmarks, "Landmark", offset, offset + sentence.length)

          ta.sentences().asScala.foreach(s => sentences += s)
          offset = offset + sentence.length
        })
      })
      sentences.toList
    }

    def SetSpRLLabels(ta: TextAnnotation, tokens: List[SpRLAnnotation], label: String, sentStart: Int, sentEnd: Int) = {
      tokens.foreach(t => {
        val start = t.getStart().intValue() - sentStart
        if (start >= 0 && start < sentEnd) {
          val startTokenId = ta.getTokenIdFromCharacterOffset(start)
          val view = ta.getView("sprl-" + label).asInstanceOf[TokenLabelView]
          val c = view.getConstituentAtToken(startTokenId)
          if (c == null)
            view.addTokenLabel(startTokenId, label, 1.0)
        }
      })
    }
  }

  def getSentenceAnnotaions(annotations: Seq[SpRLAnnotation], offset: Int, offsetEnd: Int): List[SpRLAnnotation] = {
    annotations
      .filter(x => x.getStart.intValue() >= offset && x.getStart.intValue() < offsetEnd).toList
  }
}
