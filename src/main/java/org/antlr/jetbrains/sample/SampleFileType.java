package org.antlr.jetbrains.sample;

import com.intellij.openapi.fileTypes.LanguageFileType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class SampleFileType extends LanguageFileType {
	public static final String FILE_EXTENSION = "gsql";
		public static final SampleFileType INSTANCE = new SampleFileType();

	protected SampleFileType() {
		super(SampleLanguage.INSTANCE);
	}

	@NotNull
	@Override
	public String getName() {
		return "GSQL";
	}

	@NotNull
	@Override
	public String getDescription() {
		return "GSQL";
	}

	@NotNull
	@Override
	public String getDefaultExtension() {
		return FILE_EXTENSION;
	}

	@Nullable
	@Override
	public Icon getIcon() {
		return Icons.SAMPLE_ICON;
	}
}
