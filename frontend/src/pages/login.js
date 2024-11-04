// import logo from './logo.svg';
// import './App.css';

import User from "../models/user";

function Login() {

    let user = new User();
    user.login("test@mail.com","test")

  return (
    <div className="App">
      <header className="App-header">
        {/* <img src={logo} className="App-logo" alt="logo" /> */}
        <p>
          Edit <code>src/App.js</code> and save to reload.
        </p>
        <input
            type="text"
            placeholder="Gib etwas ein..."
            value={inputValue}
            onChange={(event) => user.setUsername(event.target.value)}
            style={{ padding: '10px', fontSize: '16px', width: '300px' }}
        />
        <a
          className="App-link"
          href="https://reactjs.org"
          target="_blank"
          rel="noopener noreferrer"
        >
          Learn React
        </a>/*tesssst*/
      </header>
    </div>
  );
}

export default Login;
