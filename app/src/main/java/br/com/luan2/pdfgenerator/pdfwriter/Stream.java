//
//  Android PDF Writer
//  http://coderesearchlabs.com/androidpdfwriter
//
//  by Javier Santo Domingo (j-a-s-d@coderesearchlabs.com)
//

package br.com.luan2.pdfgenerator.pdfwriter;

public class Stream extends EnclosedContent {

    public Stream() {
        super("stream\n", "endstream\n");
    }

}
