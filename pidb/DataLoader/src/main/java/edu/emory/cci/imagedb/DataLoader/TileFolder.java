package edu.emory.cci.imagedb.DataLoader;

import java.io.File;
import java.util.List;

public interface TileFolder {
	/* get a list of possible parent folder of tileset folders*/
	public List<File> getTileFolders(File Image);
	/* get a folder that contains tiled images of the image file */
	public File getTileFolder(File tileSetFolder, File imageFile);
}