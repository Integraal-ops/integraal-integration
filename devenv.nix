{ pkgs, ...}: {
	languages = {
		java = {
			enable = true;
			jdk.package = pkgs.jdk; ## Pinned as 21
			maven = {
				enable = true;
				package = pkgs.maven;
			};
		};
	};
	env = {
		USER = "Karim";
	};
	packages = with pkgs; [
		jq
	];

	enterShell = ''
		echo Welcome to Integraal testing
	'';
}
