#initclip 0

function PaintBase(){};

PaintBase.prototype = new MovieClip();

PaintBase.prototype.onPress = function() {
  this.onPressPath[this.onPressCB](this);
}

PaintBase.prototype.onRelease = function() {
  this.onReleasePath[this.onReleaseCB](this);
  this.selected = true;
  this.onSelectPath[this.onSelectCB](this);
}

PaintBase.prototype.deselect = function() {
  this.onDeselectPath[this.onDeselectCB](this);
}

PaintBase.prototype.setOnPress = function(functionName, path) {
  if(path == undefined) {
    path = this._parent;
  }
  this.onPressCB = functionName;
  this.onPressPath = path;
}

PaintBase.prototype.setOnRelease = function(functionName, path) {
  if(path == undefined) {
    path = this._parent;
  }
  this.onReleaseCB = functionName;
  this.onReleasePath = path;
}

PaintBase.prototype.setOnSelect = function(functionName, path) {
  if(path == undefined) {
    path = this._parent;
  }
  this.onSelectCB = functionName;
  this.onSelectPath = path;
}

PaintBase.prototype.setOnDeselect = function(functionName, path) {
  if(path == undefined) {
    path = this._parent;
  }
  this.onDeselectCB = functionName;
  this.onDeselectPath = path;
}

#endinitclip
