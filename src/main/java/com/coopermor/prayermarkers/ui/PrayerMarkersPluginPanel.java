package com.coopermor.prayermarkers.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;

import com.coopermor.prayermarkers.PrayerMarker;
import com.coopermor.prayermarkers.PrayerMarkersPlugin;
import com.coopermor.prayermarkers.ui.adapters.AddMarkerMouseAdapter;
import com.coopermor.prayermarkers.ui.PrayerMarkersPanel;
import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.PluginPanel;
import net.runelite.client.ui.components.PluginErrorPanel;
import net.runelite.client.util.ImageUtil;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;


import java.awt.image.BufferedImage;
import java.util.Collection;

public class PrayerMarkersPluginPanel extends PluginPanel
{
	private static final ImageIcon ADD_ICON, PRAYER_ICON, STANDARD_ICON, ARCEUUS_ICON, ANCIENT_ICON, LUNAR_ICON;
	private final PluginErrorPanel errorPanel = new PluginErrorPanel();
	public final PrayerMarkersPlugin plugin;
	private final JPanel markerView = new JPanel();

	static
	{
		final BufferedImage addIcon = ImageUtil.loadImageResource(PrayerMarkersPlugin.class, "add_icon.png");
		ADD_ICON = new ImageIcon(addIcon);

		final BufferedImage prayerIcon = ImageUtil.loadImageResource(PrayerMarkersPlugin.class, "prayer_icon.png");
		PRAYER_ICON = new ImageIcon(prayerIcon);

		final BufferedImage standardIcon = ImageUtil.loadImageResource(PrayerMarkersPlugin.class, "standard_icon.png");
		STANDARD_ICON = new ImageIcon(standardIcon);

		final BufferedImage arceuusIcon = ImageUtil.loadImageResource(PrayerMarkersPlugin.class, "arceuus_icon.png");
		ARCEUUS_ICON = new ImageIcon(arceuusIcon);

		final BufferedImage ancientIcon = ImageUtil.loadImageResource(PrayerMarkersPlugin.class, "ancient_icon.png");
		ANCIENT_ICON = new ImageIcon(ancientIcon);

		final BufferedImage lunarIcon = ImageUtil.loadImageResource(PrayerMarkersPlugin.class, "lunar_icon.png");
		LUNAR_ICON = new ImageIcon(lunarIcon);
	}

	public PrayerMarkersPluginPanel(PrayerMarkersPlugin plugin)
	{
		this.plugin = plugin;

		setLayout(new BorderLayout());
		setBorder(new EmptyBorder(10, 10, 10, 10));
		setupErrorPanel(true);

		// title panel
		JPanel northPanel = new JPanel(new BorderLayout());
		northPanel.setBorder(new EmptyBorder(1, 0, 10, 0));

		JPanel titlePanel = new JPanel(new BorderLayout());
		titlePanel.setBorder(new EmptyBorder(1, 3, 10, 7));

		JLabel title = new JLabel("Prayer Markers");
		title.setForeground(Color.WHITE);

		JLabel markerAdd = new JLabel(ADD_ICON);
		markerAdd.setToolTipText("Add new prayer marker");
		markerAdd.addMouseListener(new AddMarkerMouseAdapter(markerAdd, plugin, this::addMarker));

		JPanel markerButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 7, 3));
		markerButtons.add(markerAdd);

		titlePanel.add(title, BorderLayout.WEST);
		titlePanel.add(markerButtons, BorderLayout.EAST);
		northPanel.add(titlePanel, BorderLayout.NORTH);

		// marker view panels, these are dynamically added in rebuild()
		JPanel centerPanel = new JPanel(new BorderLayout());
		centerPanel.setBackground(ColorScheme.DARK_GRAY_COLOR);

		markerView.setLayout(new BoxLayout(markerView, BoxLayout.Y_AXIS));
		markerView.setBackground(ColorScheme.DARK_GRAY_COLOR);
		markerView.add(errorPanel);

		centerPanel.add(markerView, BorderLayout.NORTH);

		// setup panels border layout
		add(northPanel, BorderLayout.NORTH);
		add(centerPanel, BorderLayout.CENTER);
	}

	public void rebuild()
	{
		markerView.removeAll();
		Collection<PrayerMarker> markers = plugin.getMarkers();
		assert markers != null : "call to plugin.getMarkers() = null";
		for (PrayerMarker marker : markers)
		{
			markerView.add(new PrayerMarkersPanel(plugin, marker));
			markerView.add(Box.createRigidArea(new Dimension(0, 10)));
		}
		if (markers.isEmpty())
		{
			setupErrorPanel(true);
		}
		repaint();
		revalidate();
	}

	private void addMarker()
	{
		setupErrorPanel(false);
	}

	private void setupErrorPanel(boolean enabled)
	{
		PluginErrorPanel errorPanel = this.errorPanel;
		assert errorPanel != null : "prayerMarkerErrorPanel = null";
		errorPanel.setVisible(enabled);
		if (enabled)
		{
			errorPanel.setContent("Prayer Markers", "Click the '+' button to add a prayer marker to the prayer tab.");
			markerView.removeAll();
			markerView.setLayout(new BoxLayout(markerView, BoxLayout.Y_AXIS));
			markerView.setBackground(ColorScheme.DARK_GRAY_COLOR);
			markerView.add(errorPanel);
		}
	}
}
