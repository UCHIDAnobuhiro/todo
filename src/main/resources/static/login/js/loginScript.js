function startAnimation() {
	document.querySelectorAll('.todo-item').forEach((item, index) => {
		setTimeout(() => {
			item.style.opacity = '1';

			let checkmark = item.querySelector('.checkmark');
			checkmark.style.visibility = "visible";
			checkmark.style.transition = "opacity 0.5s ease-in";
			checkmark.style.opacity = '1';

		}, index * 2000);
	});
	setTimeout(() => {
		document.querySelectorAll('.todo-item').forEach((item) => {
			item.style.opacity = '0';
			let checkmark = item.querySelector('.checkmark');
			checkmark.style.visibility = "hidden"; 
			checkmark.style.opacity = '0';
			checkmark.style.transition = ""; 
		});
		startAnimation();
	}, 6000);
}
startAnimation();