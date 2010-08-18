package org.graphstream.ui.j2dviewer.renderer.shape

import java.awt._
import java.awt.geom._

import org.graphstream.ui.geom.Point2
import org.graphstream.ui.graphicGraph._
import org.graphstream.ui.graphicGraph.stylesheet._
import org.graphstream.ui.graphicGraph.stylesheet.StyleConstants._
import org.graphstream.ui.j2dviewer._
import org.graphstream.ui.j2dviewer.renderer._
import org.graphstream.ui.util._

/**
 * Base for shapes centered around one point.
 */
trait AreaShape
	extends Shape
	with Area
	with Fillable 
	with Strokable 
	with Shadowable 
	with Decorable {
 	
	def configureForGroup( g:Graphics2D, style:Style, camera:Camera ) {
 	  	configureFillableForGroup( style, camera )
 	  	configureStrokableForGroup( style, camera )
 	  	configureShadowableForGroup( style, camera )
 	  	configureDecorableForGroup( style, camera )
 	  	configureAreaForGroup( style, camera )
 	}
 
	def configureForElement( g:Graphics2D, element:GraphicElement, info:ElementInfo, camera:Camera ) {
		configureFillableForElement( element.getStyle, camera, element )
		configureDecorableForElement( g, camera, element, info )
		configureAreaForElement( g, camera, info.asInstanceOf[NodeInfo], element, theDecor )
//		var pos = camera.getNodeOrSpritePositionGU( element, null )
//		
//		if( fit ) {
//			val decorSize = theDecor.size( g, camera, info.iconAndText )
//		
//			configureAreaForElement( g, camera, info.asInstanceOf[NodeInfo], element, pos.x, pos.y, decorSize._1, decorSize._2 )
//		} else {
//			configureAreaForElement( g, camera, info.asInstanceOf[NodeInfo], element, pos.x, pos.y )
//		}
	}
}

trait AreaOnConnectorShape
	extends Shape
	with AreaOnConnector
	with Fillable
	with Strokable
	with Shadowable {
	
	def configureForGroup( g:Graphics2D, style:Style, camera:Camera ) {
		configureFillableForGroup( style, camera )
		configureStrokableForGroup( style, camera )
		configureShadowableForGroup( style, camera )
		configureAreaOnConnectorForGroup( style, camera )
	}
	
	def configureForElement( g:Graphics2D, element:GraphicElement, info:ElementInfo, camera:Camera ) {
		configureFillableForElement( element.getStyle, camera, element )
		configureAreaOnConnectorForElement( element.asInstanceOf[GraphicEdge], element.getStyle, camera )
	}
}
 
/**
 * Base for shapes rendered between two points.
 */
trait ConnectorShape
	extends Shape
	with Connector
	with Decorable {
	
	def configureForGroup( g:Graphics2D, style:Style, camera:Camera ) {
		configureDecorableForGroup( style, camera )
		configureConnectorForGroup( style, camera )
	}
	
	def configureForElement( g:Graphics2D, element:GraphicElement, info:ElementInfo, camera:Camera ) {
		configureDecorableForElement( g, camera, element, info )
		configureConnectorForElement( g, camera, element.asInstanceOf[GraphicEdge], info.asInstanceOf[EdgeInfo] /* TODO check this ! */ )
	}
}
 
trait LineConnectorShape
	extends ConnectorShape
	with FillableLine
	with StrokableLine
	with ShadowableLine {
	
	override def configureForGroup( g:Graphics2D, style:Style, camera:Camera ) {
		configureFillableLineForGroup( style, camera )
		configureStrokableLineForGroup( style, camera )
		configureShadowableLineForGroup( style, camera )
		super.configureForGroup( g, style, camera )
	}
	
	override def configureForElement( g:Graphics2D, element:GraphicElement, info:ElementInfo, camera:Camera ) {
		configureFillableLineForElement( element.getStyle, camera, element )
		super.configureForElement( g, element, info, camera )
	}
}

trait AreaConnectorShape
	extends ConnectorShape
	with Fillable
	with Strokable
	with Shadowable {
	
	override def configureForGroup( g:Graphics2D, style:Style, camera:Camera ) {
		configureFillableForGroup( style, camera )
		configureStrokableForGroup( style, camera )
		configureShadowableForGroup( style, camera )
		super.configureForGroup( g, style, camera )
	}
	
	override def configureForElement( g:Graphics2D, element:GraphicElement, info:ElementInfo, camera:Camera ) {
		configureFillableForElement( element.getStyle, camera, element )
		super.configureForElement( g, element, info, camera )
	}}

	
trait RectangularAreaShape extends AreaShape {
	val theShape:RectangularShape
 
 	protected def make( g:Graphics2D, camera:Camera ) {
		val w = theSize.x
		val h = theSize.y
		
		theShape.setFrame( theCenter.x-w/2, theCenter.y-h/2, w, h )
 	}
 
 	protected def makeShadow( g:Graphics2D, camera:Camera ) {
		val x = theCenter.x + theShadowOff.x
		val y = theCenter.y + theShadowOff.y
		val w = theSize.x + theShadowWidth.x * 2
		val h = theSize.y + theShadowWidth.y * 2
		
		theShape.setFrame( x-w/2, y-h/2, w, h )
 	}
  
 	def renderShadow( g:Graphics2D, camera:Camera, element:GraphicElement, info:ElementInfo ) {
 		makeShadow( g, camera )
 		cast( g, theShape )
 	}
  
 	def render( g:Graphics2D, camera:Camera, element:GraphicElement, info:ElementInfo ) {
 		make( g, camera )
 		fill( g, theShape, camera )
 		stroke( g, theShape )
 		decorArea( g, camera, info.iconAndText, element, theShape )
 	}
 	
// 	override def positionAndFit( g:Graphics2D, camera:Camera, info:NodeInfo, element:GraphicElement, x:Float, y:Float ) {
// 		if( fit ) {
//			// Compute the real size and propagate it to the node info.
//			val decorSize = theDecor.size( g, camera, info.iconAndText )
//
//			if( decorSize._1 > 0 && decorSize._2 > 0 ) {
//				theSize.set( decorSize._1, decorSize._2 )
//			}
//		}
//
//		super.positionAndFit(  g, camera, info, element, x, y )
//	}
}

abstract class PolygonalShape extends AreaShape {
	var theShape = new Path2D.Float
 
 	def renderShadow( g:Graphics2D, camera:Camera, element:GraphicElement, info:ElementInfo ) {
 		makeShadow( g, camera )
 		cast( g, theShape )
 	}
  
 	def render( g:Graphics2D, camera:Camera, element:GraphicElement, info:ElementInfo ) {
 		make( g, camera )
 		fill( g, theShape, camera )
 		stroke( g, theShape )
 		decorArea( g, camera, info.iconAndText, element, theShape )
 	}
 	 	
// 	override def positionAndFit( g:Graphics2D, camera:Camera, info:NodeInfo, element:GraphicElement, x:Float, y:Float ) {
//		if( fit ) {
//			// Compute the real size and propagate it to the node info.
//			val decorSize = theDecor.size( g, camera, info.iconAndText )
//			
//			if( decorSize._1 > 0 && decorSize._2 > 0 ) {
//				theSize.set( decorSize._1, decorSize._2 )
//			}
//		}
//
//		super.positionAndFit(  g, camera, info, element, x, y )
//	}
}

class LineShape extends LineConnectorShape {
	protected var theShape:java.awt.Shape = new Line2D.Float 
// Command
  
	protected def make( g:Graphics2D, camera:Camera ) {
		val from = info.points(0)
		val to   = info.points(3)
		if( info.isCurve ) {
			val ctrl1 = info.points(1)
			val ctrl2 = info.points(2)
			val curve = new CubicCurve2D.Float
			theShape = curve
			curve.setCurve( from.x, from.y, ctrl1.x, ctrl1.y, ctrl2.x, ctrl2.y, to.x, to.y )
		} else {
			val line = new Line2D.Float
			theShape = line
			line.setLine( from.x, from.y, to.x, to.y )
		} 
	}
	protected def makeShadow( g:Graphics2D, camera:Camera ) {
		var x0 = info.points(0).x + theShadowOff.x
		var y0 = info.points(0).y + theShadowOff.y
		var x1 = info.points(3).x + theShadowOff.x
		var y1 = info.points(3).y + theShadowOff.y
		
		if( info.isCurve ) {
			var ctrlx0 = info.points(1).x + theShadowOff.x
			var ctrly0 = info.points(1).y + theShadowOff.y
			var ctrlx1 = info.points(2).x + theShadowOff.x
			var ctrly1 = info.points(2).y + theShadowOff.y
			
			val curve = new CubicCurve2D.Float
			theShape = curve
			curve.setCurve( x0, y0, ctrlx0, ctrly0, ctrlx1, ctrly1, x1, y1 )
		} else {
			val line = new Line2D.Float
			theShape = line
			line.setLine( x0, y0, x1, y1 )
		} 
	}
 
	def renderShadow( g:Graphics2D, camera:Camera, element:GraphicElement, info:ElementInfo ) {
 		makeShadow( g, camera )
 		cast( g, theShape )
	}
 
	def render( g:Graphics2D, camera:Camera, element:GraphicElement, info:ElementInfo ) {
 		make( g, camera )
 		stroke( g, theShape )
 		fill( g, theSize, theShape )
 		decorConnector( g, camera, info.iconAndText, element, theShape )
	}
}