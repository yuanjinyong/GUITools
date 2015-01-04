/**
 *
 */
package com.yuan.gui.app.tabs;

import java.awt.Dimension;

import com.yuan.gui.app.partitions.DecompliePartition;
import com.yuan.gui.app.partitions.JarSearcherPartition;
import com.yuan.gui.core.pages.AbstractPage;

/**
 * @author Yuanjy
 *
 */
public class JarTab extends AbstractPage {
	private static final long serialVersionUID = 1L;

	public JarTab() {

	}

	@Override
	protected void initFields() {
		JarSearcherPartition jarSearcher = new JarSearcherPartition();
		DecompliePartition decomplie = new DecompliePartition();

		addRow(jarSearcher, decomplie);
		addCol(jarSearcher).addCol(decomplie);
		// this.layout.linkSize(jarSearcher, decomplie);
		// this.layout.linkSize(SwingConstants.HORIZONTAL, jarSearcher, decomplie);
		// this.layout.linkSize(SwingConstants.VERTICAL, jarSearcher, decomplie);
		jarSearcher.setPreferredSize(new Dimension(getWidth() / 2, getHeight()));
		decomplie.setPreferredSize(new Dimension(getWidth() / 2, getHeight()));
	}
}
