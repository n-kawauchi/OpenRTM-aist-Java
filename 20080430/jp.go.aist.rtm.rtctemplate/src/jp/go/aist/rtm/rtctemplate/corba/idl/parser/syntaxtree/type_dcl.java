//
// Generated by JTB 1.3.2
//

package jp.go.aist.rtm.rtctemplate.corba.idl.parser.syntaxtree;

/**
 * Grammar production:
 * <PRE>
 * nodeChoice -> "typedef" type_declarator()
 *       | struct_type()
 *       | union_type()
 *       | enum_type()
 * </PRE>
 */
public class type_dcl implements Node {
   private Node parent;
   public NodeChoice nodeChoice;

   public type_dcl(NodeChoice n0) {
      nodeChoice = n0;
      if ( nodeChoice != null ) nodeChoice.setParent(this);
   }

   public void accept(jp.go.aist.rtm.rtctemplate.corba.idl.parser.visitor.Visitor v) {
      v.visit(this);
   }
   public <R,A> R accept(jp.go.aist.rtm.rtctemplate.corba.idl.parser.visitor.GJVisitor<R,A> v, A argu) {
      return v.visit(this,argu);
   }
   public <R> R accept(jp.go.aist.rtm.rtctemplate.corba.idl.parser.visitor.GJNoArguVisitor<R> v) {
      return v.visit(this);
   }
   public <A> void accept(jp.go.aist.rtm.rtctemplate.corba.idl.parser.visitor.GJVoidVisitor<A> v, A argu) {
      v.visit(this,argu);
   }
   public void setParent(Node n) { parent = n; }
   public Node getParent()       { return parent; }
}

