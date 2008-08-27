//
// Generated by JTB 1.3.2
//

package jp.go.aist.rtm.rtctemplate.corba.idl.parser.syntaxtree;

/**
 * Grammar production:
 * <PRE>
 * nodeOptional -> [ op_attribute() ]
 * op_type_spec -> op_type_spec()
 * identifier -> identifier()
 * parameter_dcls -> parameter_dcls()
 * nodeOptional1 -> [ raises_expr() ]
 * nodeOptional2 -> [ context_expr() ]
 * </PRE>
 */
public class op_dcl implements Node {
   private Node parent;
   public NodeOptional nodeOptional;
   public op_type_spec op_type_spec;
   public identifier identifier;
   public parameter_dcls parameter_dcls;
   public NodeOptional nodeOptional1;
   public NodeOptional nodeOptional2;

   public op_dcl(NodeOptional n0, op_type_spec n1, identifier n2, parameter_dcls n3, NodeOptional n4, NodeOptional n5) {
      nodeOptional = n0;
      if ( nodeOptional != null ) nodeOptional.setParent(this);
      op_type_spec = n1;
      if ( op_type_spec != null ) op_type_spec.setParent(this);
      identifier = n2;
      if ( identifier != null ) identifier.setParent(this);
      parameter_dcls = n3;
      if ( parameter_dcls != null ) parameter_dcls.setParent(this);
      nodeOptional1 = n4;
      if ( nodeOptional1 != null ) nodeOptional1.setParent(this);
      nodeOptional2 = n5;
      if ( nodeOptional2 != null ) nodeOptional2.setParent(this);
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

