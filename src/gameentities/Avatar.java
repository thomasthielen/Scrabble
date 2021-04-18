package gameentities;

/**
 * Enum to be able to choose from several preset avatars (currently only
 * placeholders).
 *
 * @author tthielen
 */
public enum Avatar {
	PIC1, PIC2, PIC3;

	@Override
	public String toString() {
		switch (this) {
		case PIC1:
			return "Placeholder: url1";
		case PIC2:
			return "Placeholder: url2";
		case PIC3:
			return "Placeholder: url3";
		default:
			throw new IllegalArgumentException();
		}
	}
}
