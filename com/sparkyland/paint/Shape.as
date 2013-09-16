#initclip

function Shape() {
  this.createEmptyMovieClip("fill", this.getNewDepth());
  this.createEmptyMovieClip("outline", this.getNewDepth());
  this.outline.col = new Color(this.outline);
  this.fill.col = new Color(this.fill);
}

Shape.prototype = new PaintBase();

Shape.prototype.create = function(w, h, outlineRGB, shape) {
  this.shape = shape;
  this.w = w;
  this.h = h;
  this.outline.rgb = outlineRGB;
  this.draw(this.outline, false);
  if(this.shape != "line") {
    this.draw(this.fill, true);
  }
}

Shape.prototype.draw = function(mc, doFill) {
  mc.clear();
  if(doFill) {
    mc.lineStyle(0, 0, 0);
    if(this.fill.rgb == undefined) {
      mc.beginFill(0, 0);
    }
    else {
      mc.beginFill(this.fill.rgb, 100);
    }
  }
  else {
    mc.lineStyle(0, this.outline.rgb, 100);
  }
  if(this.shape == "line") {
    mc.lineTo(this.w, this.h);
    return;
  }
  drawW = Math.abs(this.w);
  drawH = Math.abs(this.h);
  switch (this.shape) {
    case "ellipse":
      mc.drawEllipse(drawW/2, drawH/2);
      break;
    case "rectangle":
      mc.drawRectangle(drawW, drawH);
  }
  if(doFill) {
    endFill();
  }
  mc._x = this.w/2;
  mc._y = this.h/2;
}

Shape.prototype.toggleShowOutline = function() {
  this.outline._visible = !this.outline._visible;
}

Shape.prototype.doFill = function(rgb) {
  this.fill.rgb = rgb;
  this.draw(this.fill, true);
}

Shape.prototype.deselect = function() {
  this.outline.col.setRGB(this.outline.rgb);
  this.selected = false;
  super.deselect();
}

Shape.prototype.onRollOver = function() {
  this.outline.col.setRGB(0xFF);
}

Shape.prototype.onRollOut = function() {
  if(!this.selected) {
    this.outline.col.setRGB(this.outline.rgb);
  }
}

Shape.prototype.onPress = function() {
  super.onPress();
  if(!this.selected) {
    this.outline.col.setRGB(0xFF);
  }
}

/*Shape.prototype.onRelease = function() {
  super.onRelease();
}*/

Object.registerClass("ShapeSymbol", Shape);

#endinitclip
