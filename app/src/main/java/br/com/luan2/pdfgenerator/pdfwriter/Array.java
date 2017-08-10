//
//  Android PDF Writer
//  http://coderesearchlabs.com/androidpdfwriter
//
//  by Javier Santo Domingo (j-a-s-d@coderesearchlabs.com)
//

package br.com.luan2.pdfgenerator.pdfwriter;

public class Array extends EnclosedContent {

    public Array() {
        super("[ ", "]");
    }

    public void addItem(String s) {
        addContent(s);
        addSpace();
    }

    public void addItemsFromStringArray(String[] content) {
        for (String s : content) {
            addItem(s);
        }
    }
}
