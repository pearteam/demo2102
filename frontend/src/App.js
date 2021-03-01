import React, {Component} from 'react';
import styles from './App.module.css';
import axios from 'axios';

class App extends Component {

	constructor(props) {
		super(props);
		this.state = {
			newspapers: null,
			total: null,
			page_size: 5,
			current_page: 0,
			total_pages: 1,
			sort_by: 'id',
			selectedFile: null
		}
	};

	componentDidMount() {
		this.makeHttpRequestWithPage(0);
	}

	makeHttpRequestWithPage = async pageNumber => {
		if (pageNumber >= 0 && pageNumber < this.state.total_pages) {
			let request_url = `http://localhost:8080/api/v1/newspaper/findAllWithFilters?pageNo=${pageNumber}
			&pageSize=${this.state.page_size}
			${this.state.sort_by? '&sortBy=' + this.state.sort_by : ''}`;
			const response = await fetch(request_url, {
				method: 'GET',
				headers: {
					'Accept': 'application/json',
					'Content-Type': 'application/json',
					"Access-Control-Allow-Origin": "*",
					"Access-Control-Allow-Methods": "GET",
					"Access-Control-Allow-Headers": "Content-Type, Authorization",
				},
			});

			const data = await response.json();

			this.setState({
				newspapers: data.newspapers,
				total: data.total,
				current_page: data.current_page,
				total_pages: data.total_pages
			});
		}
	};

	changeSortingKey(key) {
		this.setState({
			sort_by: key
		});
		console.log(`Sort by: ${key}`)
		this.makeHttpRequestWithPage(0);
	}

	onChangeHandler(event) {
		console.log(event.target.files[0])
		this.setState({
			selectedFile: event.target.files[0]
		});
	};

	onClickHandler() {
		console.log("Upload clicked");
		if (this.state.selectedFile) {
			const data = new FormData();
			data.append('file', this.state.selectedFile);
			const headers = {
				"Content-Type": "multipart/form-data",
				"Access-Control-Allow-Origin": "*",
			}
			axios.post("http://localhost:8080/api/v1/uploadFile", data, {
				headers: headers
			})
				.then(res => {
					console.log(res.data.message);
					this.setState({
						selectedFile: null,
					});
					this.makeHttpRequestWithPage(this.state.total_pages - 1);
					//dirty clear input
					Array.from(document.querySelectorAll("input")).forEach(
						input => (input.value = "")
					);
				})
				.catch((error) => {
					console.log(`POST Request response error: ${error}`);
				});
		}
	};

	render() {

		let newspapers, renderPageNumbers;

		if (this.state.newspapers !== null) {
			newspapers = this.state.newspapers.map(newspaper => (
				<tr key={newspaper.id}>
					<td>{newspaper.id}</td>
					<td>{newspaper.name}</td>
					<td>{newspaper.width}</td>
					<td>{newspaper.height}</td>
					<td>{newspaper.dpi}</td>
					<td>{newspaper.fileName}</td>
					<td>{newspaper.timestamp}</td>
				</tr>
			));
		}

		const pageNumbers = [];
		if (this.state.total !== null) {
			for (let i = 0; i < Math.ceil(this.state.total / this.state.page_size); i++) {
				pageNumbers.push(i);
			}

			renderPageNumbers = pageNumbers.map(number => {
				let classes = this.state.current_page === number ? styles.active : styles.normal;

				if ((number >= this.state.current_page - 1 && number <= this.state.current_page + 1)
					|| (this.state.current_page === 0 && number === 2)
					|| (this.state.current_page === (this.state.total_pages - 1) && number === (this.state.total_pages - 3))) {
					return (
						<span key={number} className={classes}
							  onClick={() => this.makeHttpRequestWithPage(number)}>{number + 1}</span>
					);
				}
				return '';
			});
		}
		return (

			<div className={styles.App}>
				<div>
					<div className={styles.files}>
						<label>Upload Your File </label>
						<input type="file" className={styles.form_control} name="file" onChange={(event) => this.onChangeHandler(event)}/>
					</div>
					<br/>
					<span className={styles.upload} onClick={() => this.onClickHandler()}>
						Upload
					</span>
				</div>
				<br/>
				<br/>
				<br/>
				<div>
					<table className={styles.table}>
						<thead>
						<tr>
							<th onClick={() => this.changeSortingKey('id')}>Id</th>
							<th onClick={() => this.changeSortingKey('name')}>Newspaper Name</th>
							<th onClick={() => this.changeSortingKey('width')}>Width</th>
							<th onClick={() => this.changeSortingKey('height')}>Height</th>
							<th onClick={() => this.changeSortingKey('dpi')}>DPI</th>
							<th onClick={() => this.changeSortingKey('fileName')}>Filename</th>
							<th onClick={() => this.changeSortingKey('id')}>Timestamp</th>
						</tr>
						</thead>
						<tbody>
						{newspapers}
						</tbody>
					</table>
				</div>
				<div className={styles.pagination}>
					<span className={styles.pagination} onClick={() => this.makeHttpRequestWithPage(0)}>&laquo;</span>
					<span className={styles.pagination}
						  onClick={() => this.makeHttpRequestWithPage(this.state.current_page - 1)}>&lsaquo;</span>
					{renderPageNumbers}
					<span className={styles.pagination}
						  onClick={() => this.makeHttpRequestWithPage(this.state.current_page + 1)}>&rsaquo;</span>
					<span className={styles.pagination}
						  onClick={() => this.makeHttpRequestWithPage(this.state.total_pages - 1)}>&raquo;</span>
				</div>
				<br/>
				<br/>
				<br/>

			</div>
		);
	}

}

export default App;
