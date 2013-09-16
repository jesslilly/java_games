#initclip

function ToolbarButton() {
  this.selected = false;
}

ToolbarButton.prototype = new PaintBase();

ToolbarButton.prototype.create = function(name, type) {
  this.type = type;
  this.createEmptyMovieClip("btn", this.getNewDepth());
  this.btn.createEmptyMovieClip("btnHighlight", this.btn.getNewDepth());
  this.btn.createEmptyMovieClip("btnShadow", this.btn.getNewDepth());
  this.btn.createEmptyMovieClip("btnCenter", this.btn.getNewDepth());
  this.createEmptyMovieClip("symbol", this.getNewDepth());
  var w = 42;
  var h = 21;
  with(this.btn.btnHighlight) {
    lineStyle(0, 0, 0);
    beginFill(0xECECEC, 100);
    drawRectangle(w + 2, h + 2);
    endFill();
  }
  with(this.btn.btnShadow) {
    lineStyle(0, 0, 0);
    beginFill(0, 100);
    drawRectangle(w + 2, h + 2);
    endFill();
    _x += 1;
    _y += 1;
  }
  with(this.btn.btnCenter) {
    lineStyle(0, 0, 0);
    beginFill(0xDFDFDF, 100);
    drawRectangle(w, h);
    endFill();
  }
  switch(name) {
    case "select":
      with(this.symbol) {
        lineStyle(0, 0, 100);
        beginFill(0, 100);
        drawRectangle(w/4, h/9);
        drawTriangle(2 * h/3, 2 * h/3, 60, 30, -w/4);
        endFill();
        _rotation += 30;
        _x += w/8;
        _y += h/8;
      }
      break;
    case "line":
      with(this.symbol) {
        lineStyle(1, 0, 100);
        moveTo(-(w/2) + 6, -(h/2) + 6);
        lineTo((w/2) - 6, (h/2) - 6);
      }
      break;
    case "rectangle":
      with(this.symbol) {
        lineStyle(0, 0, 100);
        drawRectangle(w - 12, h - 6);
      }
    break;
    case "ellipse":
      with(this.symbol) {
        lineStyle(0, 0, 100);
        drawEllipse((w - 12)/2, (h - 6)/2);
      }
      break;
    case "text":
      this.symbol.createTextField("label", this.symbol.getNewDepth(), -w/2, -h/2, w, h);
      this.symbol.label.text = "abc";
      var tf = new TextFormat();
      tf.align = "center";
      this.symbol.label.setTextFormat(tf);
      break;
    case "fill":
      this.symbol.createTextField("label", this.symbol.getNewDepth(), -w/2, -h/2, w, h);
      this.symbol.label.text = "fill";
      var tf = new TextFormat();
      tf.align = "center";
      this.symbol.label.setTextFormat(tf);
      break;
    case "back":
      this.symbol.createTextField("label", this.symbol.getNewDepth(), -w/2, -h/2, w, h);
      this.symbol.label.text = "back";
      var tf = new TextFormat();
      tf.align = "center";
      this.symbol.label.setTextFormat(tf);
      break;
    case "forward":
      this.symbol.createTextField("label", this.symbol.getNewDepth(), -w/2, -h/2, w, h);
      this.symbol.label.text = "forward";
      var tf = new TextFormat();
      tf.align = "center";
      this.symbol.label.setTextFormat(tf);
  }
  var shiftx = this._height / 2;
  var shifty = this._width / 2;
  this.btn._y += shiftx;
  this.btn._x += shifty;
  this.symbol._y += shiftx;
  this.symbol._x += shifty;
}

ToolbarButton.prototype.select = function() {
  this.btn._width += 1;
  this.btn._height += 1;
  this.symbol._width -= 1;
  this.symbol._height -= 1;
  this._x += 1;
  this._y += 1;
  this.btn.btnHighlight._visible = false;
  this.btn.btnShadow._visible = false;
}

ToolbarButton.prototype.deselect = function() {
  this.btn._width -= 1;
  this.btn._height -= 1;
  this.symbol._width += 1;
  this.symbol._height += 1;
  this._x -= 1;
  this._y -= 1;
  this.btn.btnHighlight._visible = true;
  this.btn.btnShadow._visible = true;
}

ToolbarButton.prototype.toggleDeselect = function() {
  if(this.selected) {
    this.deSelect();
    this.selected = !this.selected;
  }
}

ToolbarButton.prototype.onPress = function() {
  if(!this.selected) {
    this.select();
    this.onSelectPath[this.onSelectCB](this);
  }
}

ToolbarButton.prototype.onRelease = function() {
  if(this.selected || this.type == "spring") {
    this.deselect();
  }
  if(this.type == "stick") {
    this.selected = !this.selected;
  }
}

Object.registerClass("ToolbarButtonSymbol", ToolbarButton);

#endinitclip
