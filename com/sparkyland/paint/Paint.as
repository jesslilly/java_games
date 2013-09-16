#include "com/sparkyland/flash/MovieClip.as"
#include "com/sparkyland/flash/DrawingMethods.as"
#include "com/sparkyland/flash/Table.as"

function init() {
  selectedShape = null;
  selectedTool = null;
  shapes = new Array();
  makeTools();
}

function makeTools( ) {
  toolbarBtnNames = ["select", "line", "rectangle", "ellipse", "text", "fill"];
  toolbarBtns = new Array();
  _root.createEmptyMovieClip("toolbar", _root.getNewDepth());
  toolbar.createEmptyMovieClip("btns", toolbar.getNewDepth());
  var name, btn;
  t = new Table(3);
  for(var i = 0; i < toolbarBtnNames.length; i++) {
    name = toolbarBtnNames[i];
    btn = toolbar.btns.attachMovie("ToolbarButtonSymbol", name + "Btn", toolbar.getNewDepth(), {val: name});
    btn.create(name, "stick");
    btn.setOnSelect("onSelectTool", _root);
    toolbarBtns.push(btn);
    t.addRow(new TableRow(0, new TableColumn(0, btn)));
  }
  toolbar.btns.attachMovie("ToolbarButtonSymbol", "backBtn", toolbar.getNewDepth(), {val: name});
  toolbar.btns.attachMovie("ToolbarButtonSymbol", "forwardBtn", toolbar.getNewDepth(), {val: name});
  toolbar.btns.backBtn.create("back", "spring");
  toolbar.btns.backBtn.setOnSelect("moveBack", _root);
  toolbar.btns.forwardBtn.create("forward", "spring");
  toolbar.btns.forwardBtn.setOnSelect("bringForward", _root);
  t.addRow(new TableRow(0, new TableColumn(0, toolbar.btns.backBtn)));
  t.addRow(new TableRow(0, new TableColumn(0, toolbar.btns.forwardBtn)));
  toolbar.attachMovie("ColorSelectorSymbol", "colSelect", toolbar.getNewDepth());
  t.addRow(new TableRow(0, new TableColumn(0, toolbar.colSelect)));
  t.render(true);
}

function onSelectTool(cmpt) {
  for(var i = 0; i < toolbarBtns.length; i++) {
    if(toolbarBtns[i] != cmpt) {
      toolbarBtns[i].toggleDeSelect();
    }
  }
  _root.selectedTool = cmpt.val;
}

function bringForward() {
  for(var i = 0; i < shapes.length; i++) {
    if(shapes[i] == selectedShape) {
      break;
    }
  }
  if(i == shapes.length - 1) {
    return;
  }
  selectedShape.swapDepths(shapes[i + 1]);
  shapes[i] = shapes[i + 1];
  shapes[i + 1] = selectedShape;
}

function moveBack() {
  for(var i = 0; i < shapes.length; i++) {
    if(shapes[i] == selectedShape) {
      break;
    }
  }
  if(i == 0) {
    return;
  }
  selectedShape.swapDepths(shapes[i - 1]);
  shapes[i] = shapes[i - 1];
  shapes[i - 1] = selectedShape;
}

function isMouseOverShapes() {
  var isOver = false;
  for(var i = 0; i < shapes.length; i++) {
    if(shapes[i].hitTest(_root._xmouse, _root._ymouse, true)) {
      isOver = true;
      break;
    }
  }
  return isOver;
}

function deSelectShapes(keepSelectedShape) {
  for(var i = 0; i < shapes.length; i++) {
    if(shapes[i] != keepSelectedShape) {
      shapes[i].deSelect();
    }
  }
  selectedShape = keepSelectedShape;
}

function removeSelected() {
  for(var i = 0; i < shapes.length; i++) {
    if(shapes[i] == selectedShape) {
      shapes.splice(i, 1);
    }
  }
  selectedShape.removeMovieClip();
}

_root.onEnterFrame = function () {
  if(this.mouseDown && this.objSelect == undefined) {
    switch(this.selectedTool) {
      case "rectangle":
      case "ellipse":
      case "line":
      case "text":
        this.doShapeDraw(false);
    }
  }
}

function onPressShape(cmpt) {
  if(selectedTool == "select") {
    cmpt.startDrag();
  }
  if(selectedTool == "fill") {
    cmpt.doFill(toolbar.colSelect.getSelectedColor());
  }
}

function onReleaseShape(cmpt) {
  cmpt.stopDrag();
}

function onSelectShape(cmpt) {
  if(selectedShape != undefined) { 
    deSelectShapes(cmpt);
  }
  selectedShape = cmpt;
}

function onDeselectShape(cmpt) {
  selectedShape = null;
}

function doShapeDraw(isFinal) {
  var w = _root._xmouse - startX;
  var h = _root._ymouse - startY;
  newShape.create(w, h, toolbar.colSelect.getSelectedColor(), selectedTool);
}

mouseListener = new Object();
mouseListener.onMouseDown = function () {
  if(!_root.toolbar.btns.hitTest(_root._xmouse, _root._ymouse)){
    _root.mouseDown = true;
    _root.startX = _root._xmouse;
    _root.startY = _root._ymouse;
    var uniqueVal = _root.getNewDepth();
    switch(_root.selectedTool) {
      case "select":
        if(!_root.isMouseOverShapes()) {
          _root.deSelectShapes();
          delete _root.selectedShape;
        }
        break;
      case "line":
      case "rectangle":
      case "ellipse":
        _root.newShape = _root.attachMovie("ShapeSymbol", "shape" + uniqueVal, uniqueVal, {_x: _root.startX, _y: _root.startY});
        _root.newShape.setOnPress("onPressShape", _root);
        _root.newShape.setOnRelease("onReleaseShape", _root);
        _root.newShape.setOnSelect("onSelectShape", _root);
        _root.newShape.setOnDeselect("onDeselectShape", _root);
        _root.shapes.push(_root.newShape);
        break;
      case "text":
        _root.newShape = _root.attachMovie("TextSymbol", "shape" + uniqueVal, uniqueVal, {_x: _root.startX, _y: _root.startY});
        _root.newShape.setOnPress("onPressShape", _root);
        _root.newShape.setOnRelease("onReleaseShape", _root);
        _root.newShape.setOnSelect("onSelectShape", _root);
        _root.newShape.setOnDeselect("onDeselectShape", _root);
        _root.shapes.push(_root.newShape);
    }
  }
}

mouseListener.onMouseUp = function () {
  _root.mouseDown = false;
}

Mouse.addListener(mouseListener);

keyListener = new Object();
keyListener.onKeyDown = function () {
  // reminder: when using test movie make sure to disable special keys
  if(Key.getCode() == Key.DELETEKEY) {
    _root.removeSelected();
  }
}

Key.addListener(keyListener);

init();