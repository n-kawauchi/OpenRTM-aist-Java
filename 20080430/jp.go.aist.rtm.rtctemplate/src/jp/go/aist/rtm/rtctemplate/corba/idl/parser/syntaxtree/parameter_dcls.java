//
// Generated by JTB 1.3.2
//

package jp.go.aist.rtm.rtctemplate.corba.idl.parser.syntaxtree;

/**
 * Grammar production:
 * <PRE>
 * nodeToken -> "("
 * nodeOptional -> [ param_dcl() ( "," param_dcl() )* ]
 * nodeToken1 -> ")"
 * </PRE>
 */
public class parameter_dcls implements Node {
   private Node parent;
   public NodeToken nodeToken;
   public NodeOptional nodeOptional;
   public NodeToken nodeToken1;

   public parameter_dcls(NodeToken n0, NodeOptional n1, NodeToken n2) {
      nodeToken = n0;
      if ( nodeToken != null ) nodeToken.setParent(this);
      nodeOptional = n1;
      if ( nodeOptional != null ) nodeOptional.setParent(this);
      nodeToken1 = n2;
      if ( nodeToken1 != null ) nodeToken1.setParent(this);
   }

   public parameter_dcls(NodeOptional n0) {
      nodeToken = new NodeToken("(");
      if ( nodeToken != null ) nodeToken.setParent(this);
      nodeOptional = n0;
      if ( nodeOptional != null ) nodeOptional.setParent(this);
      nodeToken1 = new NodeToken(")");
      if ( nodeToken1 != null ) nodeToken1.setParent(this);
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

