package tegui.text;

import tegui.TControl;

public class TText extends TControl {

    public TText(TLetter... letters) {
        for (TLetter letter : letters) {
            getChildren().add(letter);
        }
    }

}
