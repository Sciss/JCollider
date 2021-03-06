/*
 *  Control.java
 *  (JCollider)
 *
 *  Copyright (c) 2004-2015 Hanns Holger Rutz. All rights reserved.
 *
 *  This software is published under the GNU Lesser General Public License v2.1+
 *
 *
 *  For further information, please contact Hanns Holger Rutz at
 *  contact@sciss.de
 */

package de.sciss.jcollider;

/**
 *	Class for the Control UGen.
 *	This class is recognized by SynthDef
 *	in the building process. Controls
 *	should be created en bloc per rate
 *	(scalar or control), therefore the
 *	constructor takes an array of names
 *	and corresponding default values.
 *	For the sake of clarity, scalar control
 *	names should begin with a small 'i'
 *	letter, though this is not obligatory.
 *	<p>
 *	Individual controls are accessed by
 *	calling the <code>getChannel</code> method.
 *	<p>
 *	Note that when reading a synthdef from
 *	harddisc, the <code>SynthDef</code> class
 *	(as of version 0.25) will not create <code>Control</code>
 *	instances but merely plain <code>UGen</code>
 *	objects.
 *
 *  @author		Hanns Holger Rutz
 *  @version	0.33, 21-Apr-09
 */
public class Control
extends UGen
{
	protected final ControlDesc[] descs;

	private Control( String[] names, Object rate, float[] values )
	{
		this( "Control", rate, values.length, new UGenInput[0], 0 );
	
		for( int i = 0; i < values.length; i++ ) {
			descs[ i ] = new ControlDesc( names[ i ], rate, values[ i ], 0.0f );
		}
	}

	private Control( String name, Object rate, float[] values )
	{
		this( "Control", rate, values.length, new UGenInput[0], 0 );
	
		descs[ 0 ] = new ControlDesc( name, rate, values[ 0 ], 0.0f );
		for( int i = 1; i < values.length; i++ ) {
			descs[ i ] = new ControlDesc( null, rate, values[ i ], 0.0f );
		}
	}

	protected Control( String name, Object rate, int numOutputs, UGenInput[] inputs, int specialIndex )
	{
		super( name, rate, createOutputRates( numOutputs, rate ), inputs, specialIndex );

		descs	= new ControlDesc[ numOutputs ];
	}

	public UGenChannel getChannel( String name )
	{
		for( int i = 0; i < descs.length; i++ ) {
			if( descs[ i ].getName().equals( name )) return getChannel( i );
		}
		return null;
	}

	public ControlDesc getDesc( int idx )
	{
		return descs[ idx ];
	}
	
	public int getNumDescs()
	{
		return getNumOutputs();
	}
	
	private static Object[] createOutputRates( int numOutputs, Object rate )
	{
		final Object[] rates = new Object[ numOutputs ];
		for( int i = 0; i < numOutputs; i++ ) {
			rates[ i ] = rate;
		}
		return rates;
	}

	public static GraphElem ir( String name )
	{
		return ir( name, 0.0f );
	}

	/**
	 *	@warning	make sure the <code>defaultValue</code> is <code>float</code> and not <code>int</code>
	 *				since otherwise a wrong (illegal) signature is called!
	 */
	public static Control ir( String name, float defaultValue )
	{
		return new Control( name, kScalarRate, new float[] { defaultValue });
	}

	public static Control ir( String[] names, float[] values )
	{
		return new Control( names, kScalarRate, values );
	}

	public static Control ir( String name, float[] values )
	{
		return new Control( name, kScalarRate, values );
	}

	public static GraphElem kr( String name )
	{
		return kr( name, 0.0f );
	}

	public static Control kr( String name, float defaultValue )
	{
		return new Control( name, kControlRate, new float[] { defaultValue });
	}

	public static Control kr( String[] names, float[] values )
	{
		return new Control( names, kControlRate, values );
	}

	public static Control kr( String name, float[] values )
	{
		return new Control( name, kControlRate, values );
	}
}