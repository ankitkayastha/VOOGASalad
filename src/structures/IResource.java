package structures;

import exceptions.ResourceFailedException;

public interface IResource {
	boolean loaded();
	void load(String directory) throws ResourceFailedException;
}
