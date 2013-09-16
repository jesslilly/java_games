#initclip

function Text() {}

Text.prototype = new PaintBase();

Text.prototype.create = function(w, h, rgb) {
  this.createTextField("inputText", 1, 0, 0, w, h);
  this.inputText.type = "input";
  this.inputText.multiline = true;
  this.inputText.textColor = rgb;
  this.inputText.border = true;
  this.inputText.background = true;
  this.inputText.borderColor = 0xFF;
  this.inputText.onChanged = function() {
    this._parent.background._height = this._height;
    this._parent.background._width = this._width;
    this.autoSize = true;
  }
  this.inputText.onKillFocus = function(newFocus) {
    if(this.text != "") {
      this._parent.deselect();
    }
  }
  Selection.setFocus(this.inputText);
  this.selected = true;
}

Text.prototype.deselect = function() {
  super.deselect();
  this.editing = false;
  this.inputText.background = false;
  this.inputText.border = false;
  this.selected = false;
}

Text.prototype.doFill = function(rgb) {
  this.inputText.textColor = rgb;
}

Text.prototype.onRollOver = function() {
  if(!this.selected) {
    this.inputText.border = true;
  }
}

Text.prototype.onRollOut = function() {
  if(!this.selected) {
    this.inputText.border = false;
  }
}

/*Text.prototype.onEnterFrame = function() {
  if(this.editing && !(Selection.getFocus() != String(this.inputText))) {
    Selection.setFocus(this.inputText);
    Selection.setSelection(this.inputText.text.length, this.inputText.text.length);
  }
}*/

Text.prototype.onPress = function() {
  this.inputText.border = true;
  this.currentTime = getTimer();
  if(this.currentTime - this.previousTime < 500) {
    this.editing = true;
    this.inputText.background = true;
    super.deselect();
    Selection.setFocus(this.inputText);
    Selection.setSelection(0, this.inputText.length - 1);
  }
  else {
    super.onPress();
  }
  this.previousTime = this.currentTime;
}

Text.prototype.onRelease = function() {
  if(!this.editing) {
    super.onRelease();
  }
}

Object.registerClass("TextSymbol", Text);

#endinitclip
