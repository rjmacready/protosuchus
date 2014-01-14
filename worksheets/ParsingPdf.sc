import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.util.PDFTextStripper;
import org.apache.pdfbox.pdmodel.PDDocument;
import java.io.FileInputStream;

object ParsingPdf {
  val pdfParser = new PDFParser(new FileInputStream(".\\workspace\\protosuchus\\testblobs\\docs\\01.pdf"))
                                                  //> pdfParser  : org.apache.pdfbox.pdfparser.PDFParser = org.apache.pdfbox.pdfpa
                                                  //| rser.PDFParser@4590973e
  pdfParser.parse()
  val cd = pdfParser.getDocument()                //> cd  : org.apache.pdfbox.cos.COSDocument = org.apache.pdfbox.cos.COSDocument@
                                                  //| 5b27706d
  val stripper = new PDFTextStripper()            //> stripper  : org.apache.pdfbox.util.PDFTextStripper = org.apache.pdfbox.util.
                                                  //| PDFTextStripper@db8779
  val pddoc = new PDDocument(cd)                  //> pddoc  : org.apache.pdfbox.pdmodel.PDDocument = org.apache.pdfbox.pdmodel.PD
                                                  //| Document@44a5b703
	                
  val text = stripper.getText(pddoc)              //> text  : String = "Types and Programming Languages
                                                  //| Types and Programming Languages
                                                  //| Benjamin C. Pierce
                                                  //| The MIT Press
                                                  //| Cambridge, Massachusetts
                                                  //| London, England
                                                  //| ©2002 Benjamin C. Pierce
                                                  //| All rights reserved. No part of this book may be reproduced in any form by
                                                  //| any electronic of mechanical means (including photocopying, recording, or
                                                  //| information storage and retrieval) without permission in writing from the
                                                  //| publisher.
                                                  //| This book was set in Lucida Bright by the author using the LATEX document
                                                  //| preparation system.
                                                  //| Printed and bound in the United States of America.
                                                  //| Library of Congress Cataloging-in-Publication Data
                                                  //| Pierce, Benjamin C.
                                                  //| Types and programming languages / Benjamin C. Pierce
                                                  //| p. cm.
                                                  //| Includes bibliographical references and index.
                                                  //| ISBN 0-262-16209-1 (hc. : alk. paper)
                                                  //| 1. Programming languages (Electronic computers). I. Title.
                                                  //| QA76.7 .P54 2002
                                                  //| 005.13—dc21
                                                  //| 2001044428
                                                  //| C
                                                  //| Output exceeds cutoff limit.

  pddoc.close()
  cd.close()
}