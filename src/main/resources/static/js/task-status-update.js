async function updateTaskStatus(selectedElement) {
	const taskId = selectedElement.getAttribute("data-task-id");
	const newStatus = selectedElement.value;
	console.log(taskId);
	try {
		const response = await fetch(`/updateTaskStatus/${taskId}`, {
			method: "PATCH",
			headers: {
				"Content-Type": "application/json"
			},
			body: JSON.stringify({ status: newStatus })
		});

		if (response.ok) {
			console.log("ステータス更新成功", taskId, newStatus);
		} else {
			console.error("ステータス更新失敗", response.status);
		}
	} catch (error) {
		console.error("ステータス更新エラー:", error);
	}
}