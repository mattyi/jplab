package com.hzyi.jplab.model.component;

abstract class StaticComponent {
     
  private static final Field LOC_X = Field.addField("locx");
  private static final Field LOC_Y = Field.addField("locy");
  private static final Field DIR_X = Field.addField("dirx");
  private static final Field DIR_Y = Field.addField("diry");
  
  static Field locX() {
    return LOC_X;
  }

  static Field locY() {
    return LOC_Y;
  }

  static Field dirX() {
    return DIR_X;
  }

  static Field dirY() {
    return DIR_Y;
  }

} // extends Component {

//   public static class Builder<T extends Builder<T>>
//       extends Component.Builder<T> {

//     T setX(double x) {

//     }

//     T setY(double y) {

//     }

//     T setDirX(double dx) {

//     }

//     T setDirY(double dy) {

//     }

//     @Override


//     public StaticComponent build() {

//     }


//   }

// }