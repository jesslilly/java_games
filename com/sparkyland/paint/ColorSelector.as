#initclip

function ColorSelector(){
  var w = 26; 
  var h = 26;
  this.resetTransform = {ra: 100, rb: 0, ga: 100, gb: 0, ba: 100, bb: 0, aa: 100, ab: 0};
  this.selectTransform = {ra: 100, rb: 255, ga: 100, gb: 255, ba: 100, bb: 255, aa: 100, ab: 0};
  this.num = 0;
  this.selectedColor = 0;
  this.startListeners("selectedColor");
  this.createEmptyMovieClip("swatches", 1);
  this.swatches._visible = false;
  this.swatches._y = h;
  this.swatches._x += 2;
  this.createEmptyMovieClip("btn", 2);
  this.btn.createEmptyMovieClip("fill", 1);
  this.btn.createEmptyMovieClip("outline", 2);
  this.btn.createEmptyMovieClip("arrow", 3);
  this.btn.fill.lineStyle(0, 0, 0);
  this.btn.fill.beginFill(this.selectedColor, 100);
  this.btn.fill.drawRectangle(w - 6, h - 6, 0, 0, w/2 + 1, h/2 + 1);
  this.btn.fill.endFill();
  this.btn.fill.col = new Color(this.btn.fill);
  this.btn.outline.lineStyle(0, 0, 100);
  this.btn.outline.beginFill(0xDFDFDF, 100);
  this.btn.outline.drawRectangle(w - 2, w - 2, 0, 0, w/2, h/2);
  this.btn.outline.moveTo(3, 3);
  this.btn.outline.lineTo(w - 3, 3);
  this.btn.outline.lineTo(w - 3, h - 9);
  this.btn.outline.lineTo(w - 12, h - 9);
  this.btn.outline.lineTo(w - 12, h - 3);
  this.btn.outline.lineTo(3, h - 3);
  this.btn.outline.lineTo(3, 3);
  this.btn.outline.endFill();
  this.btn.arrow.lineStyle(0, 0, 100);
  this.btn.arrow.beginFill(0, 100);
  this.btn.arrow.drawTriangle(4, 4, 120, 150, w - 6, h - 6);
  this.btn.createTextField("label", 4, 0, 0, w, h);
  this.btn.label.text = "C";
  var tf = new TextFormat();
  tf.align = "center";
  tf.bold = true;
  this.btn.label.setTextFormat(tf);
  this.btn.label.selectable = false;
  this.btn.onRelease = function() {
    this._parent.swatches._visible = !this._parent.swatches._visible;
  }
  var swatch, r, g, b, rgb;
  for(var i = 0; i < 6; i++) {
    for(var j = 0; j < 6; j++) {
      for(var k = 0; k < 6; k++) {
        r = 0x33 * i;
        g = 0x33 * k; 
        b = 0x33 * j;
        rgb = (r << 16) | (g << 8) | b;
        swatch = this.createSwatch(rgb);
        swatch._y = 10*j;
        swatch._x = 10*k + (i*60);
        if(i >= 3) {
          swatch._y += 60;
          swatch._x -= 180;
         }
      }
    }
  }
}

ColorSelector.prototype = new MovieClip();

ColorSelector.prototype.createSwatch = function(rgb){
  var swatch = this.swatches.createEmptyMovieClip("swatch" + this.num, this.num);
  swatch.createEmptyMovieClip("outline", 2);
  swatch.createEmptyMovieClip("fill", 1);
  swatch.outline.lineStyle(0, 0, 100);
  swatch.outline.drawRectangle(9, 9, 0, 0, 5, 5);
  swatch.fill.lineStyle(0, 0, 0);
  swatch.fill.beginFill(rgb, 100);
  swatch.fill.drawRectangle(10, 10, 0, 0, 5, 5);
  swatch.fill.endFill();
  this.num++;
  swatch.rgb = rgb;
  swatch.col = new Color(swatch.outline);
  swatch.onRollOver = function () {
    this.col.setTransform(this._parent._parent.selectTransform);
    this._parent._parent.btn.fill.col.setRGB(this.rgb);
  }
  swatch.onRollOut = function () {
    this.col.setTransform(this._parent._parent.resetTransform);
  }
  swatch.onRelease = function () {
    this._parent._parent.selectedColor = this.rgb;
    this._parent._visible = false;
  }
  return swatch;
}

ColorSelector.prototype.getSelectedColor = function() {
  return this.selectedColor;
}

Object.registerClass("ColorSelectorSymbol", ColorSelector);

#endinitclip
