import React from "react";
import "./App.css";
import ReactSpeedometer from "react-d3-speedometer";

function App() {
  const eventSource = React.useRef<EventSource | undefined>();
  const [listening, setListening] = React.useState<boolean>(false);
  const [usageData, setUsageData] = React.useState<{
    cpuUsage: number;
    memoryUsage: number;
    date: string;
  }>({
    cpuUsage: 0,
    memoryUsage: 0,
    date: "",
  });

  React.useEffect(() => {
    if (!listening) {
      eventSource.current = new EventSource(
        "http://localhost:8080/event/resources/usage"
      );
      eventSource.current.onmessage = (event) => {
        const usage = JSON.parse(event.data);
        setUsageData(usage);
      };
      eventSource.current.onerror = (err) => {
        console.log("event source error", err);
        eventSource.current?.close();
      };
      setListening(true);

      return () => {
        eventSource.current?.close();
        console.log("event source closed");
      };
    }
  }, []);

  return (
    <div style={{ marginTop: "20px", textAlign: "center" }}>
      <h1>Dashboard</h1>
      <div style={{ display: "inline-flex" }}>
        <div style={{ margin: "50px" }}>
          <ReactSpeedometer
            maxValue={100}
            value={usageData.cpuUsage}
            valueFormat={"d"}
            customSegmentStops={[0, 25, 50, 75, 100]}
            segmentColors={["#a3be8c", "#ebcb8b", "#d08770", "#bf616a"]}
            currentValueText={"Memory Usage: ${value} %"}
            textColor={"black"}
          />
        </div>

        <div style={{ margin: "50px" }}>
          <ReactSpeedometer
            maxValue={100}
            value={usageData.memoryUsage}
            valueFormat={"d"}
            customSegmentStops={[0, 25, 50, 75, 100]}
            segmentColors={["#a3be8c", "#ebcb8b", "#d08770", "#bf616a"]}
            currentValueText={"Memory Usage: ${value} %"}
            textColor={"black"}
          />
        </div>
      </div>
    </div>
  );
}

export default App;
