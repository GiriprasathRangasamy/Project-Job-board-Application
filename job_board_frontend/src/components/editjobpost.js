
import React, { useEffect, useState } from 'react';
import {
  TextField,
  Button,
  Typography,
  Container,
  Box,
  Grid,
  MenuItem,
  Chip,
  IconButton,
} from '@mui/material';
import { LocalizationProvider, DatePicker } from '@mui/x-date-pickers';
import { AdapterDateFns } from '@mui/x-date-pickers/AdapterDateFns';
import AddIcon from '@mui/icons-material/Add';
import CancelIcon from '@mui/icons-material/Cancel';
import AdminNavbar from './adminnavbar';
import AdminFooter from './adminfooter';
import { useParams, useNavigate } from 'react-router-dom';

const EditJobPostForm = () => {
  const { id } = useParams(); // Get job ID from URL
  const [formData, setFormData] = useState({
    id:id,
    companyName: '',
    email: '',
    role: '',
    city: '',
    address: '',
    startDate: new Date(),
    applyBy: new Date(),
    salary: '',
    experience: '',
    aboutJob: '',
    skills: [],
    eligibility: [],
    aboutCompany: '',
    vacancies: '',
    jobType: '',
    applyLink: '',
  });

  const [newSkill, setNewSkill] = useState('');
  const [newEligibility, setNewEligibility] = useState('');
  const [errors, setErrors] = useState({});
  const [error, setError] = useState(null);
  const navigate = useNavigate();

  useEffect(() => {
    const fetchJobData = async () => {
      try {
        const response = await fetch(`http://localhost:8080/recruiter/jobedit/${id}`);
        if (!response.ok) {
          throw new Error('Failed to fetch job data');
        }
        const data = await response.json();
        setFormData({
          id:id,
          companyName: data.companyName,
          role: data.role,
          email: data.email,
          city: data.city,
          address: data.address,
          startDate: new Date(data.startDate),
          applyBy: new Date(data.applyBy),
          salary: data.salary,
          experience: data.experience,
          aboutJob: data.aboutJob,
          skills: data.skills,
          eligibility: data.eligibility,
          aboutCompany: data.aboutCompany,
          vacancies: data.vacancies,
          jobType: data.jobType,
          applyLink: data.applyLink,
        });
      } catch (error) {
        console.error('Error fetching job:', error);
        setError('Error fetching job details. Please try again later.');
      }
    };

    fetchJobData();
  }, []);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData({
      ...formData,
      [name]: value,
    });
  };

  const handleDateChange = (date, fieldName) => {
    setFormData({
      ...formData,
      [fieldName]: date,
    });
  };

  const handleSkillAdd = () => {
    if (newSkill && !formData.skills.includes(newSkill)) {
      setFormData({
        ...formData,
        skills: [...formData.skills, newSkill],
      });
      setNewSkill('');
    }
  };

  const handleSkillDelete = (skillToDelete) => {
    setFormData({
      ...formData,
      skills: formData.skills.filter((skill) => skill !== skillToDelete),
    });
  };

  const handleEligibilityAdd = () => {
    if (newEligibility && !formData.eligibility.includes(newEligibility)) {
      setFormData({
        ...formData,
        eligibility: [...formData.eligibility, newEligibility],
      });
      setNewEligibility('');
    }
  };

  const handleEligibilityDelete = (eligibilityToDelete) => {
    setFormData({
      ...formData,
      eligibility: formData.eligibility.filter((e) => e !== eligibilityToDelete),
    });
  };

  const validateForm = () => {
    let tempErrors = {};
    if (!formData.companyName) tempErrors.companyName = 'Company Name is required';
    if (!formData.role) tempErrors.role = 'Role is required';
    if (!formData.city) tempErrors.city = 'City is required';
    if (!formData.address) tempErrors.address = 'Address is required';
    if (!formData.startDate) tempErrors.startDate = 'Start Date is required';
    if (!formData.applyBy) tempErrors.applyBy = 'Apply By Date is required';
    if (!formData.salary) tempErrors.salary = 'Salary is required';
    if (!formData.experience) tempErrors.experience = 'Experience is required';
    if (!formData.aboutJob) tempErrors.aboutJob = 'About Job is required';
    if (formData.skills.length === 0) tempErrors.skills = 'At least one skill is required';
    if (formData.eligibility.length === 0) tempErrors.eligibility = 'At least one eligibility criterion is required';
    if (!formData.aboutCompany) tempErrors.aboutCompany = 'About Company is required';
    if (!formData.vacancies || isNaN(formData.vacancies) || parseInt(formData.vacancies) <= 0)
      tempErrors.vacancies = 'Valid number of vacancies is required';
    if (!formData.jobType) tempErrors.jobType = 'Job Type is required';
    if (!formData.applyLink) tempErrors.applyLink = 'Apply Link is required';

    setErrors(tempErrors);

    return Object.keys(tempErrors).length === 0;
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    if (validateForm()) {
      fetch("http://localhost:8080/recruiter/postjob", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(formData),
      })
        .then((response) => {
          if (response.ok) {
            alert("Job post added successfully");}
            navigate("/adminpostlist");
          });
    }
  };

  return (
    <>
    <AdminNavbar />
    <Container maxWidth="md">
      <Box
        sx={{
          mt: 4,
          p: 4,
          border: '1px solid',
          borderColor: 'grey.300',
          borderRadius: 2,
          boxShadow: 3,
          backgroundColor: 'white',
        }}
      >
        <Typography variant="h4" component="h1" gutterBottom align="center">
          Job Post Form
        </Typography>
        <form onSubmit={handleSubmit}>
          <Grid container spacing={3}>
            {/* Form fields */}
            <Grid item xs={12} sm={6}>
              <TextField
                fullWidth
                margin="normal"
                label="Company Name"
                name="companyName"
                variant="outlined"
                value={formData.companyName}
                onChange={handleChange}
                error={Boolean(errors.companyName)}
                helperText={errors.companyName}
              />
            </Grid>
            <Grid item xs={12} sm={6}>
              <TextField
                fullWidth
                margin="normal"
                label="Role"
                name="role"
                variant="outlined"
                value={formData.role}
                onChange={handleChange}
                error={Boolean(errors.role)}
                helperText={errors.role}
              />
            </Grid>
            <Grid item xs={12} sm={6}>
              <TextField
                fullWidth
                margin="normal"
                label="email"
                name="email"
                variant="outlined"
                value={formData.email}
                onChange={handleChange}
                error={Boolean(errors.role)}
                helperText={errors.role}
                disabled
              />
            </Grid>
            <Grid item xs={12} sm={6}>
              <TextField
                fullWidth
                margin="normal"
                label="City"
                name="city"
                variant="outlined"
                value={formData.city}
                onChange={handleChange}
                error={Boolean(errors.city)}
                helperText={errors.city}
              />
            </Grid>
            <Grid item xs={12}>
              <TextField
                fullWidth
                margin="normal"
                label="Address"
                name="address"
                variant="outlined"
                value={formData.address}
                onChange={handleChange}
                error={Boolean(errors.address)}
                helperText={errors.address}
                multiline
                rows={4}
              />
            </Grid>
            <Grid item xs={12} sm={6}>
              <LocalizationProvider dateAdapter={AdapterDateFns}>
                <DatePicker
                  label="Start Date"
                  value={formData.startDate}
                  onChange={(date) => handleDateChange(date, 'startDate')}
                  renderInput={(params) => (
                    <TextField
                      {...params}
                      fullWidth
                      margin="normal"
                      error={Boolean(errors.startDate)}
                      helperText={errors.startDate}
                    />
                  )}
                />
              </LocalizationProvider>
            </Grid>
            <Grid item xs={12} sm={6}>
              <LocalizationProvider dateAdapter={AdapterDateFns}>
                <DatePicker
                  label="Apply By"
                  value={formData.applyBy}
                  onChange={(date) => handleDateChange(date, 'applyBy')}
                  renderInput={(params) => (
                    <TextField
                      {...params}
                      fullWidth
                      margin="normal"
                      error={Boolean(errors.applyBy)}
                      helperText={errors.applyBy}
                    />
                  )}
                />
              </LocalizationProvider>
            </Grid>
            <Grid item xs={12} sm={6}>
              <TextField
                fullWidth
                margin="normal"
                label="Salary"
                name="salary"
                variant="outlined"
                value={formData.salary}
                onChange={handleChange}
                error={Boolean(errors.salary)}
                helperText={errors.salary}
              />
            </Grid>
            <Grid item xs={12} sm={6}>
              <TextField
                fullWidth
                margin="normal"
                label="Experience (Years)"
                name="experience"
                variant="outlined"
                value={formData.experience}
                onChange={handleChange}
                error={Boolean(errors.experience)}
                helperText={errors.experience}
                select
              >
                <MenuItem value="">Select Experience</MenuItem>
                <MenuItem value="0-1">0-1 Year</MenuItem>
                <MenuItem value="1-3">1-3 Years</MenuItem>
                <MenuItem value="3-5">3-5 Years</MenuItem>
                <MenuItem value="5+">5+ Years</MenuItem>
              </TextField>
            </Grid>
            <Grid item xs={12}>
              <TextField
                fullWidth
                margin="normal"
                label="About the Job"
                name="aboutJob"
                variant="outlined"
                multiline
                rows={4}
                value={formData.aboutJob}
                onChange={handleChange}
                error={Boolean(errors.aboutJob)}
                helperText={errors.aboutJob}
              />
            </Grid>
            <Grid item xs={12}>
              <TextField
                fullWidth
                margin="normal"
                label="Add Skill"
                variant="outlined"
                value={newSkill}
                onChange={(e) => setNewSkill(e.target.value)}
                InputProps={{
                  endAdornment: (
                    <IconButton onClick={handleSkillAdd}>
                      <AddIcon />
                    </IconButton>
                  ),
                }}
              />
              <Box sx={{ mt: 2 }}>
                {formData.skills.map((skill) => (
                  <Chip
                    key={skill}
                    label={skill}
                    onDelete={() => handleSkillDelete(skill)}
                    sx={{ mr: 1, mb: 1 }}
                  />
                ))}
                {errors.skills && (
                  <Typography color="error" variant="body2">
                    {errors.skills}
                  </Typography>
                )}
              </Box>
            </Grid>
            <Grid item xs={12}>
              <TextField
                fullWidth
                margin="normal"
                label="Add Eligibility"
                variant="outlined"
                value={newEligibility}
                onChange={(e) => setNewEligibility(e.target.value)}
                InputProps={{
                  endAdornment: (
                    <IconButton onClick={handleEligibilityAdd}>
                      <AddIcon />
                    </IconButton>
                  ),
                }}
              />
              <Box sx={{ mt: 2 }}>
                {formData.eligibility.map((e) => (
                  <Chip
                    key={e}
                    label={e}
                    onDelete={() => handleEligibilityDelete(e)}
                    sx={{ mr: 1, mb: 1 }}
                  />
                ))}
                {errors.eligibility && (
                  <Typography color="error" variant="body2">
                    {errors.eligibility}
                  </Typography>
                )}
              </Box>
            </Grid>
            <Grid item xs={12}>
              <TextField
                fullWidth
                margin="normal"
                label="About the Company"
                name="aboutCompany"
                variant="outlined"
                multiline
                rows={4}
                value={formData.aboutCompany}
                onChange={handleChange}
                error={Boolean(errors.aboutCompany)}
                helperText={errors.aboutCompany}
              />
            </Grid>
            <Grid item xs={12}>
              <TextField
                fullWidth
                margin="normal"
                label="Vacancies"
                name="vacancies"
                variant="outlined"
                value={formData.vacancies}
                onChange={handleChange}
                error={Boolean(errors.vacancies)}
                helperText={errors.vacancies}
                type="number"
              />
            </Grid>
            <Grid item xs={12}>
              <TextField
                fullWidth
                margin="normal"
                label="Job Type"
                name="jobType"
                variant="outlined"
                value={formData.jobType}
                onChange={handleChange}
                error={Boolean(errors.jobType)}
                helperText={errors.jobType}
                select
              >
                <MenuItem value="">Select Job Type</MenuItem>
                <MenuItem value="Part Time">Part Time</MenuItem>
                <MenuItem value="Full Time">Full Time</MenuItem>
                <MenuItem value="Internship">Internship</MenuItem>
              </TextField>
            </Grid>
            <Grid item xs={12}>
              <TextField
                fullWidth
                margin="normal"
                label="Apply Link"
                name="applyLink"
                variant="outlined"
                value={formData.applyLink}
                onChange={handleChange}
                error={Boolean(errors.applyLink)}
                helperText={errors.applyLink}
              />
            </Grid>
            <Grid item xs={12}>
              <Button
                type="submit"
                variant="contained"
                color="primary"
                fullWidth
                onClick={handleSubmit}
              >
                Save Changes
              </Button>
            </Grid>
          </Grid>
        </form>
      </Box>
    </Container>
    <AdminFooter/>
    </>
  );
};

export default EditJobPostForm;
