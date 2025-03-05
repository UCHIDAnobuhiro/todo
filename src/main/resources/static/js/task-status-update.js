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
			console.log("状态更新成功", taskId, newStatus);
		} else {
			console.error("状态更新失败", response.status);
		}
	} catch (error) {
		console.error("状态更新错误:", error);
	}
}
